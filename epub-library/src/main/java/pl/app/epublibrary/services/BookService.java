package pl.app.epublibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.app.epublibrary.dto.BookByAuthorDto;
import pl.app.epublibrary.exception.BookAlreadyExistsException;
import pl.app.epublibrary.exception.InvalidBookIdException;
import pl.app.epublibrary.model.book.*;
import pl.app.epublibrary.repositories.book.BookRepository;
import pl.app.epublibrary.repositories.book.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {

    private BookRepository bookRepository;
    private BookByAuthorRepository bookByAuthorRepository;
    private BookByPublisherRepository bookByPublisherRepository;
    private BookByReleaseDateRepository bookByReleaseDateRepository;
    private BookByTitleRepository bookByTitleRepository;
    private BookByUserLibraryRepository bookByUserLibraryRepository;
    private UserLibraryByBookRepository userLibraryByBookRepository;

    private CommentService commentService;
    private FileStorageService fileStorageService;

    @Autowired
    public BookService(BookRepository bookRepository,
                       BookByAuthorRepository bookByAuthorRepository,
                       BookByPublisherRepository bookByPublisherRepository,
                       BookByReleaseDateRepository bookByReleaseDateRepository,
                       BookByTitleRepository bookByTitleRepository,
                       BookByUserLibraryRepository bookByUserLibraryRepository,
                       UserLibraryByBookRepository userLibraryByBookRepository,
                       CommentService commentService,
                       FileStorageService fileStorageService) {
        this.bookRepository = bookRepository;
        this.bookByAuthorRepository = bookByAuthorRepository;
        this.bookByPublisherRepository = bookByPublisherRepository;
        this.bookByReleaseDateRepository = bookByReleaseDateRepository;
        this.bookByTitleRepository = bookByTitleRepository;
        this.bookByUserLibraryRepository = bookByUserLibraryRepository;
        this.userLibraryByBookRepository = userLibraryByBookRepository;
        this.commentService = commentService;
        this.fileStorageService = fileStorageService;
    }

// region CRUD

    public void saveBook(Book book) throws BookAlreadyExistsException {
//        Map<UUID, String> savedAuthors = new HashMap<>();
//        book.getAuthors().forEach((uuid, name) -> {
//            Author author = authorService.saveAuthor(new Author(uuid, name));
//            authorService.updateAuthor(author, book.getTitle());
//            savedAuthors.put(author.getId(), author.getName());
//        });
        //book.setAuthors(saveBookAuthors(book.getAuthors(), book.getTitle()));
        if (book.getTitle() != null && book.getAuthors() != null) {
            book = convertToLower(book);
            Book existingBook = this.getExistingBook(book);
            if (existingBook == null || !existingBook.equals(book)) {
                bookRepository.save(book);
                this.saveAllBookTables(book);
            } else {
                throw new BookAlreadyExistsException();
            }
        }
    }

    public void updateAuthor(UUID id, List<String> authors) throws InvalidBookIdException {
        Book bookToUpdate = bookRepository.findById(id).orElse(null);

        if (bookToUpdate != null) {
            //delete rows with authors of book not in the updated authors
            bookToUpdate.getAuthors().stream()
                    .filter(a -> !authors.contains(a))
                    .forEach(a -> bookByAuthorRepository.deleteByBookIdAndAuthors(id, a));

            //add rows where updated authors are different from existing authors
            authors.stream()
                    .filter(a -> !bookToUpdate.getAuthors().contains(a))
                    .forEach(a -> bookByAuthorRepository.save(new BookByAuthor(a, id, bookToUpdate.getTitle())));

            bookToUpdate.setAuthors(authors);
            bookByTitleRepository.save(
                    new BookByTitle(bookToUpdate.getTitle(), bookToUpdate.getId(), bookToUpdate.getAuthors())
            );
            bookByPublisherRepository.save(
                    new BookByPublisher(bookToUpdate.getPublisher(), bookToUpdate.getId(),
                            bookToUpdate.getTitle(), bookToUpdate.getAuthors())
            );
            bookByReleaseDateRepository.save(
                    new BookByReleaseDate(bookToUpdate.getReleaseDate(), bookToUpdate.getId(),
                            bookToUpdate.getTitle(), bookToUpdate.getAuthors())
            );
            bookRepository.save(bookToUpdate);
        } else {
            throw new InvalidBookIdException();
        }
    }

    /**
     * Deletes the book from all of the tables. Also deletes the authors of the deleted book if they don't have
     * any books
     *
     * @param id ID of the book to delete
     */
    public void deleteBook(UUID id) throws InvalidBookIdException, IOException {
        Book book = bookRepository.findById(id).orElse(null);

        if (book != null) {
            bookRepository.deleteById(id);
            bookByTitleRepository.deleteByBookIdAndTitle(id, book.getTitle());
            bookByReleaseDateRepository.deleteByBookIdAndReleaseDate(id, book.getReleaseDate());
            bookByPublisherRepository.deleteByBookIdAndPublisherName(id, book.getPublisher());

            //Delete every author of the deleted book who has no books
            book.getAuthors().forEach((a) -> bookByAuthorRepository.deleteByBookIdAndAuthors(id, a));
            //Delete the book from the user libraries
            userLibraryByBookRepository.findAllByBookId(book.getId()).forEach(entity -> {
                userLibraryByBookRepository.deleteByUsernameAndBookId(entity.getUsername(), entity.getBookId());
                bookByUserLibraryRepository.deleteByUsernameAndBookId(entity.getUsername(), entity.getBookId());
            });

            commentService.deleteAllBookComments(book.getId());
            fileStorageService.deleteCover(book.getId());
        } else {
            throw new InvalidBookIdException();
        }
//            for (String author : book.getAuthors().values()) {
//                bookByAuthorRepository.deleteByBookIdAndAuthor(id, author);
//                if (bookByAuthorRepository.findById(author).isEmpty())
//                    authorService.deleteAuthor(book.getAuthors().);
//            }
    }
//endregion

//region ENDPOINT

    public List<BookByAuthorDto> findAllBooksByAuthor(String author) {
        return bookByAuthorRepository.findAllByAuthors(author.toLowerCase())
                .stream()
                .map(BookByAuthorDto::new)
                .collect(Collectors.toList());
    }

    public Book findBookById(UUID id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<BookByTitle> findBooksByTitle(String title) {
        return bookByTitleRepository.findAllByTitle(title.toLowerCase());
    }

    public List<BookByPublisher> findBooksByPublisher(String publisher) {
        return bookByPublisherRepository.findAllByPublisherName(publisher.toLowerCase());
    }

    public BookByAuthor findBookByTitleAndAuthor(String title, String author) {
        return bookByAuthorRepository.findByAuthorsAndTitle(author.toLowerCase(), title.toLowerCase());
    }

    //TODO add paging
    public Set<String> findAllAuthors() {
//        Set<String> authors = new HashSet<>();
//        bookByAuthorRepository.findAllAuthors().forEach(author -> authors.add(author.getAuthors()));

        return bookByAuthorRepository.findAllAuthors().stream().map(BookAuthor::getAuthors).collect(Collectors.toSet());
    }

    public boolean findIfInLibrary(UUID bookId, String username) {
        return userLibraryByBookRepository.findByBookIdAndUsername(bookId, username) != null;
    }

    //TODO add paging
    public Set<String> findAllPublishers() {
        Set<String> publishers = new HashSet<>();
        bookByPublisherRepository.findAllPublishers().forEach(publisher -> publishers.add(publisher.getPublisherName()));

        return publishers;
    }

    public void addToUserLibrary(String username, UUID bookId, String title, List<String> authors) {
        bookByUserLibraryRepository.save(new BookByUserLibrary(username, bookId, title.toLowerCase(), authors));
        userLibraryByBookRepository.save(new UserLibraryByBook(username, bookId));
    }

/*    public Map<String, Integer> findAllAuthorsAndBookCount() {
        Map<String, Integer> map = new HashMap<>();
        List<Object[]> authorAndBookCount = bookByAuthorRepository.findAllAuthorsAndBookCount();
        authorAndBookCount.forEach(a -> map.put((String) a[0], (Integer) a[1]));
        return map;
    }*/
//endregion

    //region UTIL
    private Book getExistingBook(Book book) {
        //TODO: make sure it works (for now it does)
        return book
                .getAuthors()
                .stream()
//                .filter(a -> authorService.findByName(a) != null)
                .map(a -> Optional.ofNullable(bookByAuthorRepository.findByAuthorsAndTitle(a, book.getTitle())))
                .filter(Optional::isPresent)
                .map(a -> a.map(bookByAuthor -> bookRepository.getById(bookByAuthor.getBookId())))
                .findFirst()
                .orElse(Optional.empty())
                .orElse(null);


//        for (String author : book.getAuthors().values()) {
//            if (authorService.findByName(author) != null) {
//                BookByAuthor bookByAuthor = bookByAuthorRepository.findByAuthorAndTitle(author, book.getTitle());
//                if (bookByAuthor != null)
//                    return bookRepository.getById(bookByAuthor.getBookId());
//            }
//        }
//        return null;
    }

    private void saveAllBookTables(Book book) {
        book.getAuthors().forEach(
                (a) -> bookByAuthorRepository.save(new BookByAuthor(a, book.getId(), book.getTitle()))
        );
        if (book.getPublisher() != null)
            bookByPublisherRepository.save(
                    new BookByPublisher(book.getPublisher(), book.getId(), book.getTitle(), book.getAuthors()));
        if (book.getReleaseDate() != null)
            bookByReleaseDateRepository.save(
                    new BookByReleaseDate(book.getReleaseDate(), book.getId(), book.getTitle(), book.getAuthors()));
        bookByTitleRepository.save(new BookByTitle(book.getTitle(), book.getId(), book.getAuthors()));
    }

    private Book convertToLower(Book book) {
        book.setAuthors(book.getAuthors().stream().map(String::toLowerCase).collect(Collectors.toList()));
        book.setTitle(book.getTitle().toLowerCase());
        if (book.getPublisher() == null) {
            book.setPublisher("unknown publisher");
        } else {
            book.setPublisher(book.getPublisher().toLowerCase());
        }
        return book;
    }

/*    private Map<UUID, String> saveBookAuthors(Map<UUID, String> authors, String title) {
        Map<UUID, String> savedAuthors = new HashMap<>();
        authors.forEach((uuid, name) -> {
            Author author = authorService.saveAuthor(new Author(uuid, name));
            authorService.updateAuthor(author.getName(), author.getId(), title);
            savedAuthors.put(author.getId(), author.getName());
        });

        return savedAuthors;
    }*/
//endregion
}

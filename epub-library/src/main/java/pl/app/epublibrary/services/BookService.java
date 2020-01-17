package pl.app.epublibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.app.epublibrary.dto.BookByAuthorDto;
import pl.app.epublibrary.exception.BookAlreadyExistsException;
import pl.app.epublibrary.exception.InsufficientBookDataException;
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
    private BookByTitleRepository bookByTitleRepository;
    private BookByUserLibraryRepository bookByUserLibraryRepository;
    private BookByAuthorPublisherRepository bookByAuthorPublisherRepository;
    private UserLibraryByBookRepository userLibraryByBookRepository;

    private CommentService commentService;
    private FileStorageService fileStorageService;

    @Autowired
    public BookService(BookRepository bookRepository,
                       BookByAuthorRepository bookByAuthorRepository,
                       BookByPublisherRepository bookByPublisherRepository,
                       BookByTitleRepository bookByTitleRepository,
                       BookByUserLibraryRepository bookByUserLibraryRepository,
                       BookByAuthorPublisherRepository bookByAuthorPublisherRepository,
                       UserLibraryByBookRepository userLibraryByBookRepository,
                       CommentService commentService,
                       FileStorageService fileStorageService) {
        this.bookRepository = bookRepository;
        this.bookByAuthorRepository = bookByAuthorRepository;
        this.bookByPublisherRepository = bookByPublisherRepository;
        this.bookByTitleRepository = bookByTitleRepository;
        this.bookByUserLibraryRepository = bookByUserLibraryRepository;
        this.bookByAuthorPublisherRepository = bookByAuthorPublisherRepository;
        this.userLibraryByBookRepository = userLibraryByBookRepository;
        this.commentService = commentService;
        this.fileStorageService = fileStorageService;
    }

// region CRUD

    public void saveBook(Book book) throws BookAlreadyExistsException, InsufficientBookDataException {
        if (book != null && book.getTitle() != null && book.getAuthors() != null) {
            book = convertToLower(book);
            Book existingBook = this.getExistingBook(book);
            if (existingBook == null || !existingBook.equals(book)) {
                bookRepository.save(book);
                this.saveAllBookTables(book);
            } else {
                throw new BookAlreadyExistsException();
            }
        } else {
            throw new InsufficientBookDataException();
        }
    }

    public void updateAuthor(UUID id, List<String> authors) throws InvalidBookIdException {
        Book bookToUpdate = bookRepository.findById(id).orElse(null);

        if (bookToUpdate != null) {
            //delete rows with authors of book not in the updated authors
            bookToUpdate.getAuthors().stream()
                    .filter(a -> !authors.contains(a))
                    .forEach(a -> bookByAuthorRepository.deleteByTitleAndAuthors(bookToUpdate.getTitle(), a));

            //add rows where updated authors are different from existing authors
            authors.stream()
                    .filter(a -> !bookToUpdate.getAuthors().contains(a))
                    .forEach(a -> {
                        bookByAuthorRepository.save(new BookByAuthor(a, bookToUpdate.getTitle(),
                                bookToUpdate.getPublisher(), bookToUpdate.getId()));
                        bookByAuthorPublisherRepository.save(new BookByAuthorPublisher(a, bookToUpdate.getPublisher(),
                                bookToUpdate.getId(), bookToUpdate.getTitle()));
                    });

            bookToUpdate.setAuthors(authors);
            bookByTitleRepository.save(
                    new BookByTitle(bookToUpdate.getTitle(), bookToUpdate.getPublisher(),
                            bookToUpdate.getId(), bookToUpdate.getAuthors())
            );
            bookByPublisherRepository.save(
                    new BookByPublisher(bookToUpdate.getPublisher(), bookToUpdate.getTitle(), bookToUpdate.getId(),
                            bookToUpdate.getAuthors())
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
            bookByTitleRepository.deleteByBookIdAndTitleAndPublisher(id, book.getTitle(), book.getPublisher());
            bookByPublisherRepository.deleteByBookIdAndPublisherNameAndTitle(id, book.getPublisher(), book.getTitle());

            //Delete every author of the deleted book who has no books
            book.getAuthors().forEach((a) -> {
                bookByAuthorRepository.deleteByTitleAndAuthors(book.getTitle(), a);
                bookByAuthorPublisherRepository
                        .deleteByAuthorsAndPublisherAndBookId(a, book.getPublisher(), book.getId());
            });
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
    }
//endregion

//region ENDPOINT
    //TODO: add getters to the new repos
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

    public Set<String> findAllAuthors() {
        return bookByAuthorRepository.findAllAuthors().stream().map(BookAuthor::getAuthors).collect(Collectors.toSet());
    }

    public boolean findIfInLibrary(UUID bookId, String username) {
        return userLibraryByBookRepository.findByBookIdAndUsername(bookId, username) != null;
    }

    public Set<String> findAllPublishers() {
        Set<String> publishers = new HashSet<>();
        bookByPublisherRepository.findAllPublishers().forEach(publisher -> publishers.add(publisher.getPublisherName()));

        return publishers;
    }

    public void addToUserLibrary(String username, UUID bookId, String title, List<String> authors) {
        bookByUserLibraryRepository.save(new BookByUserLibrary(username, bookId, title.toLowerCase(), authors));
        userLibraryByBookRepository.save(new UserLibraryByBook(username, bookId));
    }
//endregion

    //region UTIL
    private Book getExistingBook(Book book) {
        return book
                .getAuthors()
                .stream()
                .map(a -> Optional.ofNullable(bookByAuthorRepository.findByAuthorsAndTitle(a, book.getTitle())))
                .filter(Optional::isPresent)
                .map(a -> a.map(bookByAuthor -> bookRepository.getById(bookByAuthor.getBookId())))
                .findFirst()
                .orElse(Optional.empty())
                .orElse(null);
    }

    private void saveAllBookTables(Book bookToUpdate) {
        Book book = bookToUpdate;
        if (book.getPublisher() == null) {
            book.setPublisher("unknown publisher");
        }
        book.getAuthors().forEach((a) -> {
                    bookByAuthorRepository.save(new BookByAuthor(a, book.getTitle(),
                            book.getPublisher(), book.getId()));
                    bookByAuthorPublisherRepository.save(new BookByAuthorPublisher(a, book.getPublisher(),
                            book.getId(), book.getTitle()));
                }
        );
        bookByPublisherRepository.save(
                new BookByPublisher(book.getPublisher() == null ? "unknown publisher" : book.getPublisher(),
                        book.getTitle(), book.getId(), book.getAuthors()));
        bookByTitleRepository.save(
                new BookByTitle(book.getTitle(), book.getPublisher(), book.getId(), book.getAuthors()));

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
//endregion
}

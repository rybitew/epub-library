package pl.app.epublibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.app.epublibrary.dto.BookByAuthorDto;
import pl.app.epublibrary.exceptions.*;
import pl.app.epublibrary.model.book.*;
import pl.app.epublibrary.repositories.book.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
        if (book != null && book.getTitle() != null && !book.getTitle().trim().isEmpty() && book.getAuthors() != null) {
            book = convertToLower(book);
            Book existingBook = this.getExistingBook(book);
            if (existingBook == null || !existingBook.equals(book)) {
                bookRepository.save(book);
                saveAllBookTables(book);
            } else {
                throw new BookAlreadyExistsException();
            }
        } else {
            throw new InsufficientBookDataException();
        }
    }

    public void updateAuthor(UUID id, List<String> authors)
            throws InvalidBookIdException, InsufficientParametersException {
        if (id == null) {
            throw new InvalidBookIdException();
        }
        if (authors == null || authors.isEmpty()) {
            throw new InsufficientParametersException();
        }
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
    public void deleteBook(UUID id) throws InvalidBookIdException, CannotDeleteFileException {
        if (id == null) {
            throw new InvalidBookIdException();
        }
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
    public List<BookByAuthorDto> findAllBooksByAuthor(String author) throws InsufficientParametersException {
        if (author == null || author.trim().isEmpty()) {
            throw new InsufficientParametersException();
        }
        return bookByAuthorRepository.findAllByAuthors(author.trim().toLowerCase())
                .stream()
                .map(BookByAuthorDto::new)
                .collect(Collectors.toList());
    }

    public Book findBookById(UUID id) throws InvalidBookIdException {
        if (id == null) {
            throw new InvalidBookIdException();
        }
        return bookRepository.findById(id).orElse(null);
    }

    public List<BookByTitle> findBooksByTitle(String title) throws InsufficientParametersException {
        if (title == null || title.trim().isEmpty()) {
            throw new InsufficientParametersException();
        }
        return bookByTitleRepository.findAllByTitle(title.trim().toLowerCase());
    }

    public List<BookByPublisher> findBooksByPublisher(String publisher) throws InsufficientParametersException {
        if (publisher == null || publisher.trim().isEmpty()) {
            throw new InsufficientParametersException();
        }
        return bookByPublisherRepository.findAllByPublisherName(publisher.trim().toLowerCase());
    }

    public List<BookByAuthorDto> findBookByTitleAndAuthor(String title, String author)
            throws InsufficientParametersException {
        if (title == null || title.trim().isEmpty() || author == null || author.trim().isEmpty()) {
            throw new InsufficientParametersException();
        }
        BookByAuthor entity = bookByAuthorRepository.findByAuthorsAndTitle(
                author.trim().toLowerCase(), title.trim().toLowerCase());
        if (entity != null) {
            return List.of(new BookByAuthorDto(entity));
        }
        return null;
    }

    public List<BookByPublisher> findBooksByTitleAndPublisher(String title, String publisher)
            throws InsufficientParametersException {
        if (title == null || title.trim().isEmpty() || publisher == null || publisher.trim().isEmpty()) {
            throw new InsufficientParametersException();
        }
        return bookByPublisherRepository.findAllByPublisherNameAndTitle(
                publisher.trim().toLowerCase(), title.trim().toLowerCase());
    }

    public List<BookByAuthorDto> findBooksByAuthorAndPublisher(String author, String publisher)
            throws InsufficientParametersException {
        if (author == null || author.trim().isEmpty() || publisher == null || publisher.trim().isEmpty()) {
            throw new InsufficientParametersException();
        }
        return bookByAuthorPublisherRepository.findAllByAuthorsAndPublisher(
                author.trim().toLowerCase(), publisher.trim().toLowerCase())
                .stream()
                .map(BookByAuthorDto::new)
                .collect(Collectors.toList());
    }

    public List<BookByAuthorDto> findBookByTitleAuthorAndPublisher(String title, String author, String publisher)
            throws InsufficientParametersException {
        if (title == null || title.trim().isEmpty() || publisher == null || publisher.trim().isEmpty()
                || author == null || author.trim().isEmpty()) {
            throw new InsufficientParametersException();
        }
        BookByAuthor entity = bookByAuthorRepository.findByAuthorsAndPublisherAndTitle(
                author.trim().toLowerCase(), publisher.trim().toLowerCase(), title.trim().toLowerCase());
        if (entity != null) {
            return List.of(new BookByAuthorDto(entity));
        }
        return null;
    }

    public boolean findIfInLibrary(UUID bookId, String username) throws InsufficientParametersException {
        if (bookId == null || username == null || username.trim().isEmpty()) {
            throw new InsufficientParametersException();
        }
        username = username.trim().toLowerCase();
        return userLibraryByBookRepository.findByBookIdAndUsername(bookId, username) != null;
    }

    public void addToUserLibrary(String username, UUID bookId, String title, List<String> authors)
            throws InsufficientParametersException {
        if (bookId == null || username == null || username.trim().isEmpty() || title == null || title.trim().isEmpty()
                || authors == null || authors.isEmpty()) {
            throw new InsufficientParametersException();
        }
        bookByUserLibraryRepository.save(new BookByUserLibrary(
                        username.trim(),
                        bookId,
                        title.trim().toLowerCase(),
                        authors
                                .stream()
                                .map(author -> author.trim().toLowerCase())
                                .collect(Collectors.toList())
                )
        );
        userLibraryByBookRepository.save(new UserLibraryByBook(username, bookId));
    }
//endregion

    //region UTIL
    private Book getExistingBook(Book book) throws InsufficientBookDataException {
        if (book == null) {
            throw new InsufficientBookDataException();
        }
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
        List<String> authors = book.getAuthors();
        book.setAuthors(authors.stream()
                .map(author -> author.trim().toLowerCase())
                .collect(Collectors.toList()));
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

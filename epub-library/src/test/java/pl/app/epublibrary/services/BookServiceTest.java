package pl.app.epublibrary.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.app.epublibrary.dto.BookByAuthorDto;
import pl.app.epublibrary.exceptions.*;
import pl.app.epublibrary.model.book.*;
import pl.app.epublibrary.model.user.User;
import pl.app.epublibrary.repositories.book.*;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookByAuthorRepository bookByAuthorRepository;
    @Autowired
    private BookByPublisherRepository bookByPublisherRepository;
    @Autowired
    private BookByTitleRepository bookByTitleRepository;
    @Autowired
    private BookByAuthorPublisherRepository bookByAuthorPublisherRepository;

    @Test
    void saveBookInvalid()
            throws InsufficientBookDataException, BookAlreadyExistsException, InvalidBookIdException, CannotDeleteFileException {
        Book testBook = new Book(UUID.randomUUID(), "title", List.of("author one", "author two"),
                LocalDate.now(), "publisher", null);
        testBook.setTitle(null);
        assertThrows(InsufficientBookDataException.class, () -> bookService.saveBook(testBook));
        Book testBook1 = new Book(UUID.randomUUID(), "title", List.of("author one", "author two"),
                LocalDate.now(), "publisher", null);
        testBook1.setAuthors(null);
        assertThrows(InsufficientBookDataException.class, () -> bookService.saveBook(testBook1));
        assertThrows(InsufficientBookDataException.class, () -> bookService.saveBook(null));

        Book book = new Book(UUID.randomUUID(), "title", List.of("author one", "author two"),
                LocalDate.now(), "publisher", null);
        bookService.saveBook(book);
        assertThrows(BookAlreadyExistsException.class, () -> bookService.saveBook(book));
        bookService.deleteBook(book.getId());
    }

    @Test
    void saveBookValid()
            throws InsufficientBookDataException, BookAlreadyExistsException, InvalidBookIdException,
            CannotDeleteFileException {
        Book book = new Book(UUID.randomUUID(),
                "test book",
                List.of("test author one", "test author two"),
                LocalDate.now(),
                "test publisher",
                null);

        BookByAuthor bookByAuthor1 = new BookByAuthor(book.getAuthors().get(0),
                book.getTitle(),
                book.getPublisher(),
                book.getId());
        BookByAuthor bookByAuthor2 = new BookByAuthor(book.getAuthors().get(1),
                book.getTitle(),
                book.getPublisher(),
                book.getId());

        BookByAuthorPublisher bookByAuthorPublisher1 = new BookByAuthorPublisher(book.getAuthors().get(0),
                book.getPublisher(),
                book.getId(),
                book.getTitle());
        BookByAuthorPublisher bookByAuthorPublisher2 = new BookByAuthorPublisher(book.getAuthors().get(1),
                book.getPublisher(),
                book.getId(),
                book.getTitle());

        BookByTitle bookByTitle = new BookByTitle(book.getTitle(), book.getPublisher(), book.getId(), book.getAuthors());
        BookByPublisher bookByPublisher = new BookByPublisher(book.getPublisher(), book.getTitle(),
                book.getId(), book.getAuthors());


        bookService.saveBook(book);
        assertEquals(book, bookRepository.getById(book.getId()));
        assertEquals(bookByTitle, bookByTitleRepository.findAllByTitle(book.getTitle()).get(0));
        assertEquals(bookByPublisher, bookByPublisherRepository.findAllByPublisherName(book.getPublisher()).get(0));
        assertEquals(bookByAuthor1, bookByAuthorRepository.findAllByAuthors(bookByAuthor1.getAuthors()).get(0));
        assertEquals(bookByAuthor2, bookByAuthorRepository.findAllByAuthors(bookByAuthor2.getAuthors()).get(0));
        assertEquals(bookByAuthorPublisher1, bookByAuthorPublisherRepository.findAllByAuthorsAndPublisher(
                bookByAuthorPublisher1.getAuthors(), bookByAuthorPublisher1.getPublisher()).get(0));
        assertEquals(bookByAuthorPublisher2, bookByAuthorPublisherRepository.findAllByAuthorsAndPublisher(
                bookByAuthorPublisher2.getAuthors(), bookByAuthorPublisher2.getPublisher()).get(0));
        bookService.deleteBook(book.getId());
    }

    @Test
    void updateAuthorInvalid() {
        List<String> authors = new LinkedList<>(List.of("author1", "author2"));
        assertThrows(InvalidBookIdException.class, () -> bookService.updateAuthor(null, authors));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.updateAuthor(UUID.randomUUID(), new LinkedList<>()));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.updateAuthor(UUID.randomUUID(), new LinkedList<>()));
    }

    @Test
    void updateAuthorValid()
            throws InsufficientBookDataException, BookAlreadyExistsException, InvalidBookIdException,
            InsufficientParametersException, CannotDeleteFileException {
        List<String> authors = new LinkedList<>(List.of("author1", "author2"));
        Book book = new Book(UUID.randomUUID(),
                "test book",
                List.of("test author one", "test author two"),
                LocalDate.now(),
                "test publisher",
                null);
        bookService.saveBook(book);
        bookService.updateAuthor(book.getId(), authors);
        assertEquals(authors, bookService.findBookById(book.getId()).getAuthors());
        bookService.deleteBook(book.getId());
    }

    @Test
    void deleteBookInvalid() {
        assertThrows(InvalidBookIdException.class, () -> bookService.deleteBook(null));
        assertThrows(InvalidBookIdException.class, () -> bookService.deleteBook(UUID.randomUUID()));
    }

    @Test
    void deleteBookValid()
            throws InvalidBookIdException, InsufficientBookDataException, BookAlreadyExistsException, CannotDeleteFileException {
        Book book = new Book(UUID.randomUUID(),
                "test book",
                List.of("test author one", "test author two"),
                LocalDate.now(),
                "test publisher",
                null);
        bookService.saveBook(book);
        bookService.deleteBook(book.getId());
        assertNull(bookRepository.getById(book.getId()));
        assertEquals(List.of(), bookByTitleRepository.findAllByTitle(book.getTitle()));
        assertEquals(List.of(), bookByPublisherRepository.findAllByPublisherName(book.getPublisher()));
        assertEquals(List.of(), bookByAuthorRepository.findAllByAuthors(book.getAuthors().get(0)));
        assertEquals(List.of(), bookByAuthorRepository.findAllByAuthors(book.getAuthors().get(1)));
        assertEquals(List.of(), bookByAuthorPublisherRepository.findAllByAuthorsAndPublisher(
                book.getAuthors().get(0), book.getPublisher()));
        assertEquals(List.of(), bookByAuthorPublisherRepository.findAllByAuthorsAndPublisher(
                book.getAuthors().get(1), book.getPublisher()));
    }

    @Test
    void findAllBooksByAuthorInvalid() {
        assertThrows(InsufficientParametersException.class, () -> bookService.findAllBooksByAuthor(null));
        assertThrows(InsufficientParametersException.class, () -> bookService.findAllBooksByAuthor(" "));
    }

    @Test
    void findAllBooksByAuthorValid() throws InsufficientBookDataException, BookAlreadyExistsException, InsufficientParametersException, InvalidBookIdException, CannotDeleteFileException {
        Book book = new Book(UUID.randomUUID(),
                "test book",
                List.of("test author one", "test author two"),
                LocalDate.now(),
                "test publisher",
                null);
        BookByAuthorDto bookByAuthor1 = new BookByAuthorDto(new String[]{book.getAuthors().get(0)}, book.getId(),
                book.getTitle(), book.getPublisher());
        BookByAuthorDto bookByAuthor2 = new BookByAuthorDto(new String[]{book.getAuthors().get(1)}, book.getId(),
                book.getTitle(), book.getPublisher());
        bookService.saveBook(book);
        assertEquals(bookService.findAllBooksByAuthor(bookByAuthor1.getAuthors()[0]).get(0), bookByAuthor1);
        assertEquals(bookService.findAllBooksByAuthor(bookByAuthor2.getAuthors()[0]).get(0), bookByAuthor2);
        assertEquals(bookService.findAllBooksByAuthor("Doesnt exist"), List.of());
        bookService.deleteBook(book.getId());
    }

    @Test
    void findBookByIdInvalid() {
        assertThrows(InvalidBookIdException.class, () -> bookService.findBookById(null));
    }

    @Test
    void findBookByIdValid()
            throws InsufficientBookDataException, BookAlreadyExistsException, InvalidBookIdException, CannotDeleteFileException {
        Book book = new Book(UUID.randomUUID(),
                "test book",
                List.of("test author one", "test author two"),
                LocalDate.now(),
                "test publisher",
                null);
        bookService.saveBook(book);
        assertEquals(bookService.findBookById(book.getId()), book);
        assertNull(bookService.findBookById(UUID.randomUUID()));
        bookService.deleteBook(book.getId());
    }

    @Test
    void findBooksByTitleInvalid() {
        assertThrows(InsufficientParametersException.class, () -> bookService.findBooksByTitle(null));
        assertThrows(InsufficientParametersException.class, () -> bookService.findBooksByTitle(" "));
    }

    @Test
    void findBooksByTitleValid()
            throws InsufficientBookDataException, BookAlreadyExistsException, InsufficientParametersException,
            InvalidBookIdException, CannotDeleteFileException {
        Book book = new Book(UUID.randomUUID(),
                "test book",
                List.of("test author one", "test author two"),
                LocalDate.now(),
                "test publisher",
                null);
        BookByTitle bookByTitle =
                new BookByTitle(book.getTitle(), book.getPublisher(), book.getId(), book.getAuthors());
        bookService.saveBook(book);
        assertEquals(bookService.findBooksByTitle(book.getTitle()).get(0), bookByTitle);
        assertEquals(bookService.findBooksByTitle(" " + book.getTitle() + " ").get(0), bookByTitle);
        assertEquals(bookService.findBooksByTitle("Doesnt exist"), List.of());
        bookService.deleteBook(book.getId());
    }

    @Test
    void findBooksByPublisherInvalid() {
        assertThrows(InsufficientParametersException.class, () -> bookService.findBooksByPublisher(null));
        assertThrows(InsufficientParametersException.class, () -> bookService.findBooksByPublisher(" "));
    }

    @Test
    void findBooksByPublisherValid()
            throws InsufficientBookDataException, BookAlreadyExistsException, InsufficientParametersException,
            InvalidBookIdException, CannotDeleteFileException {
        Book book = new Book(UUID.randomUUID(),
                "test book",
                List.of("test author one", "test author two"),
                LocalDate.now(),
                "test publisher",
                null);
        Book book2 = new Book(UUID.randomUUID(),
                "test book2",
                List.of("test author one2", "test author two2"),
                LocalDate.now(),
                "test publisher",
                null);
        BookByPublisher bookByPublisher =
                new BookByPublisher(book.getPublisher(), book.getTitle(), book.getId(), book.getAuthors());
        bookService.saveBook(book);
        assertEquals(bookService.findBooksByPublisher(book.getPublisher()).get(0), bookByPublisher);
        assertEquals(bookService.findBooksByPublisher(" " + book.getPublisher() + " ").get(0), bookByPublisher);
        assertEquals(bookService.findBooksByPublisher("Doesnt exist"), List.of());

        bookService.saveBook(book2);
        assertEquals(bookService.findBooksByPublisher(bookByPublisher.getPublisherName()).size(), 2);
        bookService.deleteBook(book.getId());
        bookService.deleteBook(book2.getId());
    }

    @Test
    void findBookByTitleAndAuthorInvalid() {
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBookByTitleAndAuthor(null, "author"));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBookByTitleAndAuthor(" ", "author"));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBookByTitleAndAuthor("title", null));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBookByTitleAndAuthor("title", " "));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBookByTitleAndAuthor(" ", " "));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBookByTitleAndAuthor(null, null));
    }

    @Test
    void findBookByTitleAndAuthorValid()
            throws InsufficientParametersException, InsufficientBookDataException, BookAlreadyExistsException,
            InvalidBookIdException, CannotDeleteFileException {
        Book book = new Book(UUID.randomUUID(),
                "test book",
                List.of("test author one", "test author two"),
                LocalDate.now(),
                "test publisher",
                null);
        bookService.saveBook(book);
        BookByAuthorDto testBook1 = new BookByAuthorDto(new String[]{book.getAuthors().get(0)}, book.getId(),
                book.getTitle(), book.getPublisher());
        BookByAuthorDto testBook2 = new BookByAuthorDto(new String[]{book.getAuthors().get(1)}, book.getId(),
                book.getTitle(), book.getPublisher());

        assertEquals(bookService.findBookByTitleAndAuthor(testBook1.getTitle(), testBook1.getAuthors()[0]).get(0),
                testBook1);
        assertEquals(bookService.findBookByTitleAndAuthor(testBook2.getTitle(), testBook2.getAuthors()[0]).get(0),
                testBook2);
        assertEquals(bookService.findBooksByTitle("doesnt exist"), List.of());

        bookService.deleteBook(book.getId());
    }

    @Test
    void findBooksByTitleAndPublisherInvalid() {
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBooksByTitleAndPublisher(null, "publisher"));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBooksByTitleAndPublisher(" ", "publisher"));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBooksByTitleAndPublisher("title", null));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBooksByTitleAndPublisher("title", " "));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBooksByTitleAndPublisher(" ", " "));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBooksByTitleAndPublisher(null, null));
    }

    @Test
    void findBooksByTitleAndPublisherValid()
            throws InsufficientParametersException, InsufficientBookDataException, BookAlreadyExistsException,
            InvalidBookIdException, CannotDeleteFileException {
        Book book = new Book(UUID.randomUUID(),
                "test book",
                List.of("test author one", "test author two"),
                LocalDate.now(),
                "test publisher",
                null);
        bookService.saveBook(book);
        BookByPublisher testBook =
                new BookByPublisher(book.getPublisher(), book.getTitle(), book.getId(), book.getAuthors());

        assertEquals(bookService.findBooksByTitleAndPublisher(book.getTitle(), book.getPublisher()).get(0), testBook);
        assertEquals(bookService.findBooksByTitleAndPublisher(" doesnt exist", "another"), List.of());

        bookService.deleteBook(book.getId());
    }

    @Test
    void findBooksByAuthorAndPublisherInvalid() {
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBooksByAuthorAndPublisher(null, "publisher"));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBooksByAuthorAndPublisher(" ", "publisher"));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBooksByAuthorAndPublisher("author", null));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBooksByAuthorAndPublisher("author", " "));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBooksByAuthorAndPublisher(" ", " "));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBooksByAuthorAndPublisher(null, null));
    }

    @Test
    void findBooksByAuthorAndPublisherValid()
            throws InsufficientBookDataException, BookAlreadyExistsException, InsufficientParametersException,
            CannotDeleteFileException, InvalidBookIdException {
        Book book = new Book(UUID.randomUUID(),
                "test book",
                List.of("test author one", "test author two"),
                LocalDate.now(),
                "test publisher",
                null);
        BookByAuthorDto testBook1 = new BookByAuthorDto(new String[]{book.getAuthors().get(0)}, book.getId(),
                book.getTitle(), book.getPublisher());
        BookByAuthorDto testBook2 = new BookByAuthorDto(new String[]{book.getAuthors().get(1)}, book.getId(),
                book.getTitle(), book.getPublisher());
        bookService.saveBook(book);
        assertEquals(bookService.findBooksByAuthorAndPublisher(
                testBook1.getAuthors()[0], testBook1.getPublisher()).get(0), testBook1);
        assertEquals(bookService.findBooksByAuthorAndPublisher(
                testBook2.getAuthors()[0], testBook2.getPublisher()).get(0), testBook2);
        assertEquals(bookService.findBooksByAuthorAndPublisher(
                "Doesnt exist a", "doesnt exist p"), List.of());

        bookService.deleteBook(book.getId());
    }

    @Test
    void findBookByTitleAuthorAndPublisherInvalid() {
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBookByTitleAuthorAndPublisher(null, "author", "publisher"));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBookByTitleAuthorAndPublisher(" ", "author", "publisher"));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBookByTitleAuthorAndPublisher("title", null, "publisher"));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBookByTitleAuthorAndPublisher("title", " ", "publisher"));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBookByTitleAuthorAndPublisher("title", "author", " "));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBookByTitleAuthorAndPublisher("title", "author", null));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBookByTitleAuthorAndPublisher(" ", " ", " "));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findBookByTitleAuthorAndPublisher(null, null, null));
    }

    @Test
    void findBookByTitleAuthorAndPublisherValid()
            throws InsufficientBookDataException, BookAlreadyExistsException, InsufficientParametersException,
            CannotDeleteFileException, InvalidBookIdException {
        Book book = new Book(UUID.randomUUID(),
                "test book",
                List.of("test author one", "test author two"),
                LocalDate.now(),
                "test publisher",
                null);
        BookByAuthorDto testBook1 = new BookByAuthorDto(new String[]{book.getAuthors().get(0)}, book.getId(),
                book.getTitle(), book.getPublisher());
        BookByAuthorDto testBook2 = new BookByAuthorDto(new String[]{book.getAuthors().get(1)}, book.getId(),
                book.getTitle(), book.getPublisher());
        bookService.saveBook(book);
        assertEquals(bookService.findBookByTitleAuthorAndPublisher(
                testBook1.getTitle(), testBook1.getAuthors()[0], testBook1.getPublisher()).get(0), testBook1);
        assertEquals(bookService.findBookByTitleAuthorAndPublisher(
                testBook2.getTitle(), testBook2.getAuthors()[0], testBook2.getPublisher()).get(0), testBook2);
        assertNull(bookService.findBookByTitleAuthorAndPublisher(
                "invalid", "Doesnt exist a", "doesnt exist p"));

        bookService.deleteBook(book.getId());
    }

    @Test
    void findIfInLibraryInvalid() {
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findIfInLibrary(null, "user"));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findIfInLibrary(UUID.randomUUID(), " "));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.findIfInLibrary(UUID.randomUUID(), null));
    }

    @Test
    void findIfInLibraryValid()
            throws InsufficientBookDataException, BookAlreadyExistsException, InsufficientParametersException,
            CannotDeleteFileException, InvalidBookIdException, InvalidUsernameException, InvalidEmailException,
            InvalidEmailFormatException, InvalidPasswordException, InvalidUsernameOrBookIdException {
        Book book = new Book(UUID.randomUUID(),
                "test book",
                List.of("test author one", "test author two"),
                LocalDate.now(),
                "test publisher",
                null);
        User user = new User("test", "test", "test@test.t", false);
        bookService.saveBook(book);
        userService.saveUser(user);
        bookService.addToUserLibrary(user.getUsername(), book.getId(), book.getTitle(), book.getAuthors());

        assertTrue(bookService.findIfInLibrary(book.getId(), user.getUsername()));
        assertFalse(bookService.findIfInLibrary(UUID.randomUUID(), user.getUsername()));
        assertFalse(bookService.findIfInLibrary(UUID.randomUUID(), "false"));

        bookService.deleteBook(book.getId());
        userService.deleteUser(user.getUsername());
    }

    @Test
    void addToUserLibraryInvalid() {
        assertThrows(InsufficientParametersException.class,
                () -> bookService.addToUserLibrary(null, UUID.randomUUID(), "title", List.of("author")));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.addToUserLibrary("user", null, "title", List.of("author")));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.addToUserLibrary("user", UUID.randomUUID(), null, List.of("author")));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.addToUserLibrary("user", UUID.randomUUID(), null, List.of()));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.addToUserLibrary("user", UUID.randomUUID(), "title", null));
        assertThrows(InsufficientParametersException.class,
                () -> bookService.addToUserLibrary(" ", UUID.randomUUID(), "title", List.of("author")));
    }

    @Test
    void addToUserLibraryValid()
            throws InsufficientBookDataException, BookAlreadyExistsException, InsufficientParametersException,
            CannotDeleteFileException, InvalidBookIdException, InvalidUsernameException, InvalidEmailException,
            InvalidEmailFormatException, InvalidPasswordException, InvalidUsernameOrBookIdException {
        Book book = new Book(UUID.randomUUID(),
                "test book",
                List.of("test author one", "test author two"),
                LocalDate.now(),
                "test publisher",
                null);
        User user = new User("test", "test", "test@test.t", false);
        bookService.saveBook(book);
        userService.saveUser(user);
        bookService.addToUserLibrary(user.getUsername(), book.getId(), book.getTitle(), book.getAuthors());
        assertTrue(bookService.findIfInLibrary(book.getId(), user.getUsername()));

        bookService.deleteBook(book.getId());
        userService.deleteUser(user.getUsername());
    }
}

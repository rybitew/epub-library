package pl.app.epublibrary.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.app.epublibrary.exceptions.*;
import pl.app.epublibrary.model.book.Book;
import pl.app.epublibrary.model.book.BookByUserLibrary;
import pl.app.epublibrary.model.user.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Test
    void saveUserTestInvalid()
            throws InvalidUsernameException, InvalidEmailException, InvalidEmailFormatException,
            InvalidUsernameOrBookIdException, InvalidPasswordException {
        assertThrows(InvalidEmailFormatException.class,
                () -> userService.saveUser(new User("test", "test", "test", false)));
        assertThrows(InvalidEmailFormatException.class,
                () -> userService.saveUser(new User("test", "test", "test@", false)));
        assertThrows(InvalidEmailFormatException.class,
                () -> userService.saveUser(new User("test", "test", "@test.test", false)));
        assertThrows(InvalidEmailFormatException.class,
                () -> userService.saveUser(new User("test", "test", "test@test", false)));
        assertThrows(InvalidEmailFormatException.class,
                () -> userService.saveUser(new User("test", "test", ".test", false)));

        assertThrows(InvalidUsernameException.class,
                () -> userService.saveUser(new User(null, "test", "test@test.test", false)));
        assertThrows(InvalidPasswordException.class,
                () -> userService.saveUser(new User("test", null, "test@test.test", false)));
        assertThrows(InvalidEmailFormatException.class,
                () -> userService.saveUser(new User("test", "test", null, false)));

        assertThrows(InvalidUsernameException.class,
                () -> userService.saveUser(new User(" ", "test", "test@test.test", false)));
        assertThrows(InvalidPasswordException.class,
                () -> userService.saveUser(new User("test", " ", "test@test.test", false)));
        assertThrows(InvalidEmailFormatException.class,
                () -> userService.saveUser(new User("test", "test", " ", false)));

        assertThrows(InvalidUsernameException.class,
                () -> userService.saveUser(null));

        userService.deleteUser("test");
        User testUser = new User("test", "test", "test@test.test", false);
        userService.saveUser(testUser);
        User user = new User("test1", "test", "test@test.test", false);
        assertThrows(InvalidEmailException.class,
                () -> userService.saveUser(user));

        testUser = new User("test1", "test", "test1@test.test", false);
        userService.saveUser(testUser);
        assertThrows(InvalidUsernameException.class,
                () -> userService.saveUser(user));

        userService.deleteUser("test");
        userService.deleteUser("test1");
    }

    @Test
    void saveUserTestValid()
            throws InvalidUsernameException, InvalidEmailFormatException, InvalidEmailException,
            InvalidUsernameOrBookIdException, InvalidPasswordException {
        userService.deleteUser("test");
        userService.saveUser(new User("test", "test", "test@test.test", null));
        userService.deleteUser("test");
        userService.saveUser(new User("test", "test", "test@test.test", true));
        userService.deleteUser("test");

        userService.saveUser(new User("test", "test", "test.t@test.test", true));
        userService.deleteUser("test");
        userService.saveUser(new User("test", "test", "test21@test.test", true));
        userService.deleteUser("test");
        userService.saveUser(new User("test", "test", "test21@test2.test", true));
        userService.deleteUser("test");
        userService.saveUser(new User("test", "test", "te.s.t@test2.test", true));
        userService.deleteUser("test");
    }

    @Test
    void deleteUserTestInvalid() {
        assertThrows(InvalidUsernameException.class,
                () -> userService.deleteUser(null));
        assertThrows(InvalidUsernameException.class,
                () -> userService.deleteUser(" "));
    }

    @Test
    void deleteUserTestValid()
            throws InvalidUsernameOrBookIdException, InvalidUsernameException, InvalidPasswordException,
            InvalidEmailFormatException, InvalidEmailException {
        userService.deleteUser("test");
        userService.saveUser(new User("test", "test", "test@test.test", null));
        userService.deleteUser("test");
    }

    @Test
    void elevateUserInvalid() throws InvalidUsernameException {
        assertThrows(InvalidUsernameException.class,
                () -> userService.elevateUser(null));
        assertThrows(InvalidUsernameException.class,
                () -> userService.elevateUser(" "));
        userService.elevateUser("not found");
    }

    @Test
    void elevateUserValid() throws InvalidUsernameException, InvalidPasswordException, InvalidEmailFormatException,
            InvalidEmailException, InvalidUsernameOrBookIdException {
        userService.deleteUser("test");
        userService.saveUser(new User("test", "test", "test@test.test", null));
        userService.elevateUser("test");
        userService.deleteUser("test");
    }

    @Test
    void isUserElevatedInvalid() throws InvalidUsernameException {

        userService.isUserElevated("not");
        assertThrows(InvalidUsernameException.class, () -> userService.isUserElevated(" "));
        assertThrows(InvalidUsernameException.class, () -> userService.isUserElevated(null));
    }

    @Test
    void isUserElevatedValid()
            throws InvalidUsernameException, InvalidUsernameOrBookIdException, InvalidPasswordException,
            InvalidEmailFormatException, InvalidEmailException {
        userService.saveUser(new User("elevated", "test", "testemail1@test.test", null));
        userService.saveUser(new User("not_elevated", "test", "testemail2@test.test", null));
        userService.isUserElevated("elevated");
        userService.isUserElevated("not_elevated");
        userService.deleteUser("elevated");
        userService.deleteUser("not_elevated");
    }

    @Test
    void findByUsernameInvalid() throws InvalidUsernameException {

        assertNull(userService.findUserByUsername("not_valid"));
        assertThrows(InvalidUsernameException.class, () -> userService.findUserByUsername(" "));
        assertThrows(InvalidUsernameException.class, () -> userService.findUserByUsername(null));
    }

    @Test
    void findByUsernameValid()
            throws InvalidUsernameException, InvalidPasswordException, InvalidEmailFormatException,
            InvalidEmailException, InvalidUsernameOrBookIdException {
        userService.deleteUser("valid");
        User valid = new User("valid", "test", "test1@test.test", true);
        userService.saveUser(valid);

        assertEquals(userService.findUserByUsername("valid"), valid);
        assertNull(userService.findUserByUsername("invalid"));
        userService.deleteUser(valid.getUsername());
    }

    @Test
    void findByEmailInvalid() throws InvalidEmailException {

        assertThrows(InvalidEmailException.class, () -> userService.findUserByEmail(" "));
        assertNull(userService.findUserByEmail("not_valid"));
        assertThrows(InvalidEmailException.class, () -> userService.findUserByEmail(null));
    }

    @Test
    void findByEmailValid()
            throws InvalidEmailException, InvalidUsernameException, InvalidEmailFormatException,
            InvalidPasswordException, InvalidUsernameOrBookIdException {
        userService.deleteUser("valid");
        User valid = new User("valid", "test", "test1@test.test", true);
        userService.saveUser(valid);

        assertEquals(valid, userService.findUserByEmail(valid.getEmail()));
        assertNull(userService.findUserByEmail("not_valid"));
        userService.deleteUser(valid.getUsername());
    }

    @Test
    void deleteFromUserLibraryInvalid()
            throws InvalidUsernameOrBookIdException, InvalidUsernameException, InvalidPasswordException,
            InvalidEmailFormatException, InvalidEmailException {
        userService.deleteUser("valid");
        User user = new User("valid", "test", "test1@test.test", false);
        userService.saveUser(user);

        assertThrows(InvalidUsernameOrBookIdException.class,
                () -> userService.deleteFromUserLibrary(user.getUsername(), null));
        assertThrows(InvalidUsernameOrBookIdException.class,
                () -> userService.deleteFromUserLibrary(" ", null));
        assertThrows(InvalidUsernameOrBookIdException.class,
                () -> userService.deleteFromUserLibrary(null, UUID.randomUUID()));

        userService.deleteUser(user.getUsername());
    }

    @Test
    void deleteFromUserLibraryValid()
            throws InvalidUsernameException, InvalidEmailException, InvalidEmailFormatException,
            InvalidPasswordException, InsufficientBookDataException, BookAlreadyExistsException,
            InsufficientParametersException, InvalidUsernameOrBookIdException, UnexpectedErrorException,
            InvalidBookIdException, CannotDeleteFileException, CannotDeleteFileException {
        Book book = new Book(UUID.randomUUID(), "book", List.of("author one"), LocalDate.now(),
                "publisher", null);
        userService.deleteUser("valid");
        User user = new User("valid", "test", "test1@test.test", false);
        userService.saveUser(user);
        bookService.saveBook(book);
        bookService.addToUserLibrary(user.getUsername(), book.getId(), book.getTitle(), book.getAuthors());

        userService.deleteFromUserLibrary(user.getUsername(), book.getId());
        assertEquals(userService.findAllUserLibraryBooks(user.getUsername()), Set.of());
        userService.deleteFromUserLibrary("invalid", UUID.randomUUID());

        bookService.deleteBook(book.getId());
        userService.deleteUser(user.getUsername());
    }

    @Test
    void findAllUserLibraryBooksInvalid() {
        assertThrows(InvalidUsernameException.class, () -> userService.findAllUserLibraryBooks(null));
        assertThrows(InvalidUsernameException.class, () -> userService.findAllUserLibraryBooks(" "));
    }

    @Test
    void findAllUserLibraryBooksValid()
            throws InvalidUsernameOrBookIdException, InvalidUsernameException, InsufficientBookDataException,
            BookAlreadyExistsException, InvalidPasswordException, InvalidEmailFormatException, InvalidEmailException,
            InsufficientParametersException, InvalidBookIdException, CannotDeleteFileException {
        Book book = new Book(UUID.randomUUID(), "book", List.of("author one"), LocalDate.now(),
                "publisher", null);
        User userNotEmpty = new User("valid", "test", "test1@test.test", false);
        User userEmpty = new User("empty", "test", "test2@test.test", false);
        BookByUserLibrary bookByUserLibrary =
                new BookByUserLibrary(userNotEmpty.getUsername(), book.getId(), book.getTitle(), book.getAuthors());

        userService.saveUser(userNotEmpty);
        bookService.saveBook(book);
        bookService.addToUserLibrary(userNotEmpty.getUsername(), book.getId(), book.getTitle(), book.getAuthors());

        assertEquals(Set.of(bookByUserLibrary), userService.findAllUserLibraryBooks(userNotEmpty.getUsername()));
        assertEquals(Set.of(), userService.findAllUserLibraryBooks(userEmpty.getUsername()));

        bookService.deleteBook(book.getId());
        userService.deleteUser("valid");
        userService.deleteUser("empty");
    }
}

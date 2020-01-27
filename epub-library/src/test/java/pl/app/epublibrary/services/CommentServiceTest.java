package pl.app.epublibrary.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.app.epublibrary.exceptions.*;
import pl.app.epublibrary.model.book.Book;
import pl.app.epublibrary.model.comment.Comment;
import pl.app.epublibrary.model.user.User;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;
    @Autowired
    private CommentService commentService;

    @Test
    void saveCommentInvalid() {
        Comment comment = new Comment(UUID.randomUUID(), "user", UUID.randomUUID(), Instant.now(),
                "title", "content");
        Comment testComment2 = comment;
        testComment2.setUsername(null);
        assertThrows(InvalidEntityException.class,
                () -> commentService.saveComment(testComment2));
        Comment testComment3 = comment;
        testComment2.setUsername(" ");
        assertThrows(InvalidEntityException.class,
                () -> commentService.saveComment(testComment3));

        Comment testComment4 = comment;
        testComment4.setBookId(null);
        assertThrows(InvalidEntityException.class,
                () -> commentService.saveComment(testComment4));

        Comment testComment5 = comment;
        testComment4.setTitle(null);
        assertThrows(InvalidEntityException.class,
                () -> commentService.saveComment(testComment5));
        Comment testComment6 = comment;
        testComment4.setTitle(" ");
        assertThrows(InvalidEntityException.class,
                () -> commentService.saveComment(testComment6));
    }

    @Test
    void saveCommentValid()
            throws InsufficientBookDataException, BookAlreadyExistsException, InvalidEmailException,
            InvalidPasswordException, InvalidEntityException, InvalidUsernameException, InvalidBookIdException,
            InvalidUsernameOrBookIdException, InvalidEmailFormatException, CannotDeleteFileException {
        Book book = new Book(UUID.randomUUID(),
                "test book",
                List.of("test author one", "test author two"),
                LocalDate.now(),
                "test publisher",
                null);
        User user = new User("test", "test", "test@test.t", false);
        Comment comment = new Comment(UUID.randomUUID(), user.getUsername(), book.getId(), Instant.now(),
                book.getTitle(), "content");
        bookService.saveBook(book);
        userService.saveUser(user);
        commentService.saveComment(comment);

        assertNotEquals(commentService.findAllCommentsByUser(user.getUsername()), List.of());

        bookService.deleteBook(book.getId());
        userService.deleteUser(user.getUsername());
    }

    @Test
    void deleteCommentInvalid() {
        Comment comment = new Comment(UUID.randomUUID(), "user", UUID.randomUUID(),
                Instant.now(), null, null);
        assertThrows(InvalidEntityException.class, () -> commentService.deleteComment(null));
        Comment testComment1 = comment;
        testComment1.setBookId(null);
        assertThrows(InvalidEntityException.class, () -> commentService.deleteComment(testComment1));
        Comment testComment2 = comment;
        testComment2.setId(null);
        assertThrows(InvalidEntityException.class, () -> commentService.deleteComment(testComment2));
        Comment testComment3 = comment;
        testComment3.setTimestamp(null);
        assertThrows(InvalidEntityException.class, () -> commentService.deleteComment(testComment3));
    }

    @Test
    void deleteCommentValid() throws InvalidBookIdException, InvalidEntityException, InvalidUsernameException {
        Comment comment = new Comment(UUID.randomUUID(), "user", UUID.randomUUID(), Instant.now(),
                "title", "content");
        commentService.saveComment(comment);
        assertNotEquals(commentService.findAllCommentsByBook(comment.getBookId()), List.of());
        assertNotEquals(commentService.findAllCommentsByUser(comment.getUsername()), List.of());
        commentService.deleteComment(comment);
        assertEquals(commentService.findAllCommentsByBook(comment.getBookId()), List.of());
        assertEquals(commentService.findAllCommentsByUser(comment.getUsername()), List.of());
    }

    @Test
    void deleteAllBookCommentsInvalid() {
        assertThrows(InvalidBookIdException.class, () -> commentService.deleteAllBookComments(null));
    }

    @Test
    void deleteAllBookCommentsValid() throws InvalidEntityException, InvalidBookIdException {
        Comment comment = new Comment(UUID.randomUUID(), "user", UUID.randomUUID(), Instant.now(),
                "title", "content");
        Comment comment2 = new Comment(UUID.randomUUID(), "user", comment.getBookId(), Instant.now(),
                "title", "content");
        commentService.saveComment(comment);
        commentService.saveComment(comment2);
        assertEquals(commentService.findAllCommentsByBook(comment.getBookId()).size(), 2);
        commentService.deleteAllBookComments(comment.getBookId());
        assertEquals(commentService.findAllCommentsByBook(comment.getBookId()), List.of());

    }

    @Test
    void deleteAllUserCommentsInvalid() {
        assertThrows(InvalidUsernameException.class, () -> commentService.deleteAllUserComments(null));
        assertThrows(InvalidUsernameException.class, () -> commentService.deleteAllUserComments(" "));
        assertThrows(InvalidUsernameException.class, () -> commentService.deleteAllUserComments(""));
    }

    @Test
    void deleteAllUserCommentsValid() throws InvalidEntityException, InvalidBookIdException, InvalidUsernameException {
        Comment comment = new Comment(UUID.randomUUID(), "user", UUID.randomUUID(), Instant.now(),
                "title", "content");
        Comment comment2 = new Comment(UUID.randomUUID(), comment.getUsername(), comment.getBookId(), Instant.now(),
                "title", "content");
        commentService.saveComment(comment);
        commentService.saveComment(comment2);
        assertEquals(commentService.findAllCommentsByUser(comment.getUsername()).size(), 2);
        commentService.deleteAllBookComments(comment.getBookId());
        assertEquals(commentService.findAllCommentsByUser(comment.getUsername()), List.of());
    }

    @Test
    void findAllCommentsByUserInvalid() {
        assertThrows(InvalidUsernameException.class, () -> commentService.findAllCommentsByUser(null));
        assertThrows(InvalidUsernameException.class, () -> commentService.findAllCommentsByUser(" "));
        assertThrows(InvalidUsernameException.class, () -> commentService.findAllCommentsByUser(""));
    }

    @Test
    void findAllCommentsByUserValid() throws InvalidEntityException, InvalidUsernameException {
        Comment comment = new Comment(UUID.randomUUID(), "user", UUID.randomUUID(), Instant.now(),
                "title", "content");
        Comment comment2 = new Comment(UUID.randomUUID(), comment.getUsername(), comment.getBookId(), Instant.now(),
                "title", "content");
        commentService.saveComment(comment);
        commentService.saveComment(comment2);
        assertEquals(commentService.findAllCommentsByUser(comment.getUsername()).size(), 2);
        commentService.deleteAllUserComments(comment.getUsername());
    }

    @Test
    void findAllCommentsByBookInvalid() {
        assertThrows(InvalidBookIdException.class, () -> commentService.deleteAllBookComments(null));
    }

    @Test
    void findAllCommentsByBookValid() throws InvalidEntityException, InvalidBookIdException {
        Comment comment = new Comment(UUID.randomUUID(), "user", UUID.randomUUID(), Instant.now(),
                "title", "content");
        Comment comment2 = new Comment(UUID.randomUUID(), comment.getUsername(), comment.getBookId(), Instant.now(),
                "title", "content");
        commentService.saveComment(comment);
        commentService.saveComment(comment2);
        assertEquals(commentService.findAllCommentsByBook(comment.getBookId()).size(), 2);
        commentService.deleteAllBookComments(comment.getBookId());
    }
}
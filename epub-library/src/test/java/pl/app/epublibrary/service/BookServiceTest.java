package pl.app.epublibrary.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.app.epublibrary.exceptions.BookAlreadyExistsException;
import pl.app.epublibrary.exceptions.InsufficientBookDataException;
import pl.app.epublibrary.exceptions.InsufficientParametersException;
import pl.app.epublibrary.model.book.*;
import pl.app.epublibrary.repositories.book.*;
import pl.app.epublibrary.services.BookService;
import pl.app.epublibrary.services.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookServiceTest {

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
    void saveBookInvalid() throws InsufficientBookDataException, BookAlreadyExistsException {
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
    }

    @Test
    void saveBookValid() throws InsufficientBookDataException, BookAlreadyExistsException {
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
    }
}

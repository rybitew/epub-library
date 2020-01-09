package pl.app.epublibrary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.app.epublibrary.exception.InvalidBookIdException;
import pl.app.epublibrary.model.book.*;
import pl.app.epublibrary.services.BookService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/authors/", params = {"author"})
    public @ResponseBody
    List<BookByAuthor> getBooksByAuthor(@RequestParam(value = "author") String author) {
        return bookService.findAllBooksByAuthor(author);
    }

    @GetMapping(value = "/books/", params = {"id"})
    public @ResponseBody
    Book getBookById(@RequestParam(value = "id") String id) {
        return bookService.findBookById(UUID.fromString(id));
    }

    @GetMapping(value = "/books/", params = {"title"})
    public @ResponseBody
    List<BookByTitle> getBooksByTitle(@RequestParam(value = "title") String title) {
        return bookService.findBooksByTitle(title);
    }

    @GetMapping(value = "/books/", params = {"publisher"})
    public @ResponseBody
    List<BookByPublisher> getBooksByPublisher(@RequestParam(value = "publisher") String publisher) {
        return bookService.findBooksByPublisher(publisher);
    }

    @PostMapping(value = "books/change-author/", params = {"id", "authors"})
    public String changeAuthors(
            @RequestParam(value = "id") String id, @RequestParam(value = "authors") List<String> authors) {
        try {
            bookService.updateAuthor(UUID.fromString(id), authors);
            return "OK";
        } catch (InvalidBookIdException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Unknown Error", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error", e);
        }
    }

    @GetMapping(value = "/books/", params = {"title", "author"})
    public @ResponseBody
    List<BookByAuthor> getBookByTitleAndAuthor(
            @RequestParam(value = "title") String title, @RequestParam(value = "author") String author) {
        return List.of(bookService.findBookByTitleAndAuthor(title, author));
    }

    @GetMapping(value = "/authors/all")
    public @ResponseBody
    Set<String> getAllAuthors() {
        return bookService.findAllAuthors();
    }

    @GetMapping(value = "/publishers/all")
    public @ResponseBody
    Set<String> getAllPublishers() {
        return bookService.findAllPublishers();
    }

    @DeleteMapping(value = "/books/delete/", params = {"id"})
    public void deleteBookById(@RequestParam(value = "id") String id) {
        try {
            bookService.deleteBook(UUID.fromString(id));
        } catch (InvalidBookIdException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Invalid book ID.", e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Unknown Error", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error", e);
        }
    }

    @PostMapping(value = "/books/add-to-library/", params = {"id", "user", "title"})
    public void addToUserLibrary(@RequestParam(value = "id") String bookId,
                                 @RequestParam(value = "user") String username,
                                 @RequestParam(value = "title") String title) {
        bookService.addToUserLibrary(username, UUID.fromString(bookId), title);
    }
}

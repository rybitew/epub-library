package pl.app.epublibrary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.app.epublibrary.model.book.*;
import pl.app.epublibrary.services.BookService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping(value = "/authors/", params = {"author"})
    public @ResponseBody
    List<BookByAuthor> getBooksByAuthor(@RequestParam(value = "author") String author) {
        return bookService.findAllBooksByAuthor(author);
    }

    @RequestMapping(value = "/books/", params = {"id"})
    public @ResponseBody
    Book getBookById(@RequestParam(value = "id") String id) {
        return bookService.findBookById(UUID.fromString(id));
    }

    @RequestMapping(value = "/books/", params = {"title"})
    public @ResponseBody
    List<BookByTitle> getBooksByTitle(@RequestParam(value = "title") String title) {
        return bookService.findBooksByTitle(title);
    }

    @RequestMapping(value = "/books/", params = {"publisher"})
    public @ResponseBody
    List<BookByPublisher> getBooksByPublisher(@RequestParam(value = "publisher") String publisher) {
        return bookService.findBooksByPublisher(publisher);
    }

    @RequestMapping(value = "books/change-author/", params = {"id", "authors"}, method = RequestMethod.GET)
    public String changeAuthors(
            @RequestParam(value = "id") String id, @RequestParam(value = "authors") List<String> authors) {
        return bookService.updateAuthor(UUID.fromString(id), authors) ? "OK" : "ERROR";
    }

    @RequestMapping(value = "/books/", params = {"title", "author"})
    public @ResponseBody
    BookByAuthor getBookByTitleAndAuthor(
            @RequestParam(value = "title") String title, @RequestParam(value = "author") String author) {
        return bookService.findBookByTitleAndAuthor(title, author);
    }

    @RequestMapping(value = "/authors/all")
    public @ResponseBody
    Set<String> getAllAuthors() {
        return bookService.findAllAuthors();
    }

    @RequestMapping(value = "/publishers/all")
    public @ResponseBody
    Set<String> getAllPublishers() {
        return bookService.findAllPublishers();
    }
}

package pl.app.epublibrary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.app.epublibrary.model.book.BookByAuthor;
import pl.app.epublibrary.services.BookService;

import java.util.List;

@RestController
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/authors/{author}")
    public List<BookByAuthor> getBooksByAuthor(@PathVariable String author) {
        return bookService.findAllBooksByAuthor(author);
    }
}

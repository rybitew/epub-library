package pl.app.epublibrary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.app.epublibrary.dto.BookByAuthorDto;
import pl.app.epublibrary.exceptions.CannotDeleteFileException;
import pl.app.epublibrary.exceptions.InsufficientParametersException;
import pl.app.epublibrary.exceptions.InvalidBookIdException;
import pl.app.epublibrary.model.book.Book;
import pl.app.epublibrary.model.book.BookByPublisher;
import pl.app.epublibrary.model.book.BookByTitle;
import pl.app.epublibrary.services.BookService;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/authors/", params = {"author"})
    public @ResponseBody
    List<BookByAuthorDto> getBooksByAuthor(@RequestParam(value = "author") String author) {
        try {
            return bookService.findAllBooksByAuthor(author);
        } catch (InsufficientParametersException e) {
                        throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "One of the parameters is empty.", e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error", e);
        }
    }
//region FIND BOOK
    @GetMapping(value = "/books/", params = {"id"})
    public @ResponseBody
    Book getBookById(@RequestParam(value = "id") String id) {
        try {
            return bookService.findBookById(UUID.fromString(id));
        } catch (InvalidBookIdException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Book ID is null.", e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid book ID.", e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error", e);
        }
    }

    @GetMapping(value = "/books/", params = {"title"})
    public @ResponseBody
    List<BookByTitle> getBooksByTitle(@RequestParam(value = "title") String title) {
        try {
            return bookService.findBooksByTitle(title);
        } catch (InsufficientParametersException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "One of the parameters is empty.", e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error", e);
        }
    }

    @GetMapping(value = "/books/", params = {"publisher"})
    public @ResponseBody
    List<BookByPublisher> getBooksByPublisher(@RequestParam(value = "publisher") String publisher) {
        try {
            return bookService.findBooksByPublisher(publisher);
        } catch (InsufficientParametersException e) {
                        throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "One of the parameters is empty.", e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error", e);
        }
    }

    @GetMapping(value = "/books/", params = {"title", "author"})
    public @ResponseBody
    List<BookByAuthorDto> getBookByTitleAndAuthor(
            @RequestParam(value = "title") String title, @RequestParam(value = "author") String author) {
        try {
            return bookService.findBookByTitleAndAuthor(title, author);
        } catch (InsufficientParametersException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "One of the parameters is empty.", e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error", e);
        }
    }

    @GetMapping(value = "/books/", params = {"title", "publisher"})
    public @ResponseBody
    List<BookByPublisher> getBookByTitleAndPublisher(
            @RequestParam(value = "title") String title, @RequestParam(value = "publisher") String publisher) {
        try {
            return bookService.findBooksByTitleAndPublisher(title, publisher);
        } catch (InsufficientParametersException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "One of the parameters is empty.", e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error", e);
        }
    }

    @GetMapping(value = "/books/", params = {"author", "publisher"})
    public @ResponseBody
    List<BookByAuthorDto> getBookByAuthorAndPublisher(
            @RequestParam(value = "author") String author, @RequestParam(value = "publisher") String publisher) {
        try {
            return bookService.findBooksByAuthorAndPublisher(author, publisher);
        } catch (InsufficientParametersException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "One of the parameters is empty.", e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error", e);
        }
    }

    @GetMapping(value = "/books/", params = {"title", "author", "publisher"})
    public @ResponseBody
    List<BookByAuthorDto> getBookByTitleAuthorAndPublisher(@RequestParam(value = "title") String title,
                                                           @RequestParam(value = "author") String author,
                                                           @RequestParam(value = "publisher") String publisher) {
        try {
            return bookService.findBookByTitleAuthorAndPublisher(title, author, publisher);
        } catch (InsufficientParametersException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "One of the parameters is empty.", e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error", e);
        }
    }
//endregion

    @GetMapping(value = "books/library/", params = {"id", "username"})
    public boolean checkIfInLibrary(@RequestParam(value = "id") String bookId,
                                    @RequestParam(value = "username") String username) {
        try {
            return bookService.findIfInLibrary(UUID.fromString(bookId), username);
        } catch (InsufficientParametersException e) {
                        throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "One of the parameters is empty.", e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid book ID.", e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error", e);
        }
    }

    @PutMapping(value = "books/change-author/")
    public String changeAuthors(
    @RequestBody BookByAuthorDto book) {
        try {
            bookService.updateAuthor(UUID.fromString(book.getBookId().toString()),
                    Arrays.stream(book.getAuthors()).map(String::toLowerCase).collect(Collectors.toList()));
            return "OK";
        } catch (InsufficientParametersException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No book authors specified.", e);
        } catch (InvalidBookIdException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Book ID is null.", e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid book ID.", e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error", e);
        }
    }

    @DeleteMapping(value = "/books/delete/", params = {"id"})
    public void deleteBookById(@RequestParam(value = "id") String id) {
        try {
            bookService.deleteBook(UUID.fromString(id));
        } catch (InvalidBookIdException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Book ID is null.", e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid book ID.", e);
        } catch (CannotDeleteFileException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot delete the book cover.", e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error", e);
        }
    }

    @PostMapping(value = "/books/add-to-library/", params = {"id", "user", "title"})
    public void addToUserLibrary(@RequestParam(value = "id") String bookId,
                                 @RequestParam(value = "user") String username,
                                 @RequestParam(value = "title") String title,
                                 @RequestBody List<String> authors) {
        try {
            bookService.addToUserLibrary(username, UUID.fromString(bookId), title, authors);
        } catch (InsufficientParametersException e) {
                        throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "One of the parameters is empty.", e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid book ID.", e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error", e);
        }
    }
}


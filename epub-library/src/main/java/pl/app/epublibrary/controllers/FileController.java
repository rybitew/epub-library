package pl.app.epublibrary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pl.app.epublibrary.exceptions.*;
import pl.app.epublibrary.model.book.Book;
import pl.app.epublibrary.services.BookService;
import pl.app.epublibrary.services.FileStorageService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileController {

    private UUID bookId;

    private FileStorageService fileStorageService;
    private BookService bookService;

    @Autowired
    public FileController(FileStorageService fileStorageService, BookService bookService) {
        this.fileStorageService = fileStorageService;
        this.bookService = bookService;
    }

    @PostMapping("/book/upload/")
    public HttpStatus uploadFile(@RequestParam(value = "file") MultipartFile file) {
        try {
            Book book = fileStorageService.storeFile(file);
            bookId = book.getId();
            bookService.saveBook(book);
            return HttpStatus.OK;
        } catch (InsufficientBookDataException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Uploaded file does not contain author or title.", e);
        } catch (FileSaveErrorException e) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Could not save file, try again later.", e);
        } catch (InvalidFileException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Could not process file.", e);
        } catch (BookAlreadyExistsException e) {
            try {
                fileStorageService.deleteCover(bookId);
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Book already exists.", e);
            } catch (CannotDeleteFileException ex) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Cannot delete a file.", e);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error.", e);
        }
    }

    @GetMapping(value = "books/cover/", params = {"path"}, produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] getBookCover(@RequestParam(value = "path") String path) {
        try {
            FileInputStream in = new FileInputStream(path);
            byte[] bytes = in.readAllBytes();
            in.close();
            return bytes;
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Image not found", e);
        }
    }
}
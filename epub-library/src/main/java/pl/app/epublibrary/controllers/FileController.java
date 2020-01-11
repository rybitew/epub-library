package pl.app.epublibrary.controllers;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pl.app.epublibrary.exception.BookAlreadyExistsException;
import pl.app.epublibrary.exception.FileSaveErrorException;
import pl.app.epublibrary.exception.InvalidFileException;
import pl.app.epublibrary.exception.UnexpectedErrorException;
import pl.app.epublibrary.model.book.Book;
import pl.app.epublibrary.services.BookService;
import pl.app.epublibrary.services.FileStorageService;
import pl.app.epublibrary.util.MetadataReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
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
    public String uploadFile(@RequestParam(value = "file") MultipartFile file) {
        try {
            Book book = fileStorageService.storeFile(file);
            bookId = book.getId();
            bookService.saveBook(book);
            return "OK";
//        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/downloadFile/")
//                .path(fileName)
//                .toUriString();
//
//        return new UploadFileResponse(fileName, fileDownloadUri,
//                file.getContentType(), file.getSize());
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
                return "Book exists";
            } catch (IOException ex) {
                e.printStackTrace();
                return "Book exists";
            }
        } catch (Exception e) {
            e.printStackTrace();

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error", e);
        }
    }

    @GetMapping(value = "books/cover/", params = {"path"}, produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] getBookCover(@RequestParam(value = "path") String path) {
        try {
            FileInputStream in = new FileInputStream(path);
            return in.readAllBytes();
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Image not found", e);
        }
    }
}
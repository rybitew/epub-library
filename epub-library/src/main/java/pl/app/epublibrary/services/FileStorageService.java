package pl.app.epublibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.app.epublibrary.config.FileStorageProperties;
import pl.app.epublibrary.exceptions.CannotCreateDirectoryException;
import pl.app.epublibrary.exceptions.FileSaveErrorException;
import pl.app.epublibrary.exceptions.InvalidFileException;
import pl.app.epublibrary.exceptions.InvalidFileNameException;
import pl.app.epublibrary.model.book.Book;
import pl.app.epublibrary.util.MetadataReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private MetadataReader metadataReader;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties,
                              MetadataReader metadataReader)
            throws CannotCreateDirectoryException {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        this.metadataReader = metadataReader;

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new CannotCreateDirectoryException();
        }
    }

    public Book storeFile(MultipartFile file) throws FileSaveErrorException, InvalidFileException {
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new InvalidFileNameException();
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            metadataReader.setBook(targetLocation.toString());

            UUID bookId = UUID.randomUUID();
            Book book = new Book(bookId,
                    metadataReader.getTitle(),
                    metadataReader.getAuthors(),
                    metadataReader.getReleaseDate(),
                    metadataReader.getPublisher(),
                    metadataReader.getCoverImagePath(bookId));

            deleteFile(targetLocation);
            return book;
        } catch (IOException | InvalidFileNameException ex) {
            throw new FileSaveErrorException();
        } catch (NullPointerException e) {
            throw new InvalidFileException();
        }
    }

    public void deleteCover(UUID bookId) throws IOException {
        try {
            Files.deleteIfExists(Paths.get("./covers/" + bookId.toString() + ".png"));
        } catch (IOException e) {
            deleteCover(bookId);
        }
    }

    private void deleteFile(Path location) {
        try {
            Files.deleteIfExists(location);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


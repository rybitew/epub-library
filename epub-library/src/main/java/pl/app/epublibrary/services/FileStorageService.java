package pl.app.epublibrary.services;

import com.adobe.epubcheck.api.EpubCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.app.epublibrary.config.FileStorageProperties;
import pl.app.epublibrary.exceptions.*;
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
        if (file == null) {
            throw new InvalidFileException();
        }
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            if (fileName.contains("..")) {
                throw new InvalidFileNameException();
            }

            // Copy-replace file to the target location
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            EpubCheck epubCheck = new EpubCheck(targetLocation.toFile());
            if (!epubCheck.validate()) {
                throw new InvalidFileException();
            }
            metadataReader.setBook(targetLocation.toString());

            UUID bookId = UUID.randomUUID();
            Book book = new Book(bookId,
                    metadataReader.getTitle(),
                    metadataReader.getAuthors(),
                    metadataReader.getReleaseDate(),
                    metadataReader.getPublisher(),
                    metadataReader.getCoverImagePath(bookId));
            file.getInputStream().close();
            deleteFile(targetLocation);
            return book;
        } catch (IOException | InvalidFileNameException ex) {
            throw new FileSaveErrorException();
        } catch (NullPointerException | InvalidFileException e) {
            throw new InvalidFileException();
        }
    }

    public void deleteCover(UUID bookId) throws CannotDeleteFileException {
        try {
            Files.deleteIfExists(Paths.get("./covers/" + bookId.toString() + ".png"));
        } catch (IOException e) {
            throw new CannotDeleteFileException();
        }
    }

    private void deleteFile(Path location) throws IOException {
        Files.deleteIfExists(location);
    }
}


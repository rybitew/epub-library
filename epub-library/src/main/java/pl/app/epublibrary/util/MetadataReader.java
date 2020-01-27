package pl.app.epublibrary.util;

import com.adobe.epubcheck.api.EpubCheck;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Date;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipFile;

@Component
public class MetadataReader {

    private EpubReader epubReader;
    private Book book;

    @Autowired
    public MetadataReader(EpubReader epubReader) {
        this.epubReader = epubReader;
    }

    public boolean setBook(String path) {
        try {
            book = epubReader.readEpub(new FileInputStream(path));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public String getTitle() {
        return book.getTitle();
    }

    public List<String> getAuthors() {
        List<String> authors = new LinkedList<>();
        for (Author author : book.getMetadata().getAuthors()) {
            authors.add(author.getFirstname() + " " + author.getLastname());
        }
        return authors;
    }

    public String getPublisher() {
        return book.getMetadata().getPublishers().get(0);
    }

    public LocalDate getReleaseDate() {
        try {
            return LocalDate.parse(book.getMetadata().getDates().stream()
                    .filter(date -> date.getEvent() == Date.Event.PUBLICATION)
                    .findFirst().orElse(null).getValue());
        } catch (NullPointerException e) {
            return null;
        }
    }

    public String getCoverImagePath(UUID bookId) {
        return saveCoverToFile(book.getCoverImage(), bookId);
    }

    private String saveCoverToFile(Resource file, UUID bookId) {
        try {

            InputStream is = file.getInputStream();
            BufferedImage image = ImageIO.read(is);
            File directory = new File("./covers");
            if (!directory.exists()) {
                Files.createDirectory(Path.of("./covers"));
            }
            String path = "./covers/" + bookId.toString() + ".png";
            File imageFile = new File(path);
            path = imageFile.getCanonicalPath();
            ImageIO.write(image, "png", imageFile);
            path = path.replace("\\", "/");

            return path;
        } catch (IOException e) {
            return null;
        }
    }
}

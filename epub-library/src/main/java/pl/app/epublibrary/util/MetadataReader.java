package pl.app.epublibrary.util;

import nl.siegmann.epublib.domain.*;
import nl.siegmann.epublib.epub.EpubReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

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
            book = epubReader.readEpub(new FileInputStream(".\\books\\" + path));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getTitle() {
        return book.getTitle();
    }

    public List<String> getAuthors() {
        List<String> authors = new LinkedList<>();
        for (Author author : book.getMetadata().getAuthors()) {
            authors.add(author.getFirstname() + author.getLastname());
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

    public Resource getCoverImage() {
        return book.getCoverImage();
    }
}

package pl.app.epublibrary.entities;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Table("books")
public class Book {

    @PrimaryKeyColumn(
            ordinal = 0,
            type = PrimaryKeyType.PARTITIONED
    )
    private UUID bookId;

    @PrimaryKeyColumn(
            ordinal = 1,
            type = PrimaryKeyType.PARTITIONED
    )
    private String title;

    private List<String> authorSurnames;
    private List<String> authorNames;

    private LocalDate releaseDate;
    private String genre;

    public Book(UUID bookId, String title, List<String> authorSurnames, List<String> authorNames, LocalDate releaseDate, String genre) {
        this.bookId = bookId;
        this.title = title;
        this.authorSurnames = authorSurnames;
        this.authorNames = authorNames;
        this.releaseDate = releaseDate;
        this.genre = genre;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthorSurname() {
        return authorSurnames;
    }

    public void setAuthorSurname(List<String> authorSurname) {
        this.authorSurnames = authorSurname;
    }

    public List<String> getAuthorNames() {
        return authorNames;
    }

    public void setAuthorNames(List<String> authorNames) {
        this.authorNames = authorNames;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}

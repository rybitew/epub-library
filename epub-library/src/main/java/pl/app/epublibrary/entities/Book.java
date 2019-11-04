package pl.app.epublibrary.entities;

import com.datastax.driver.core.DataType;
import com.datastax.driver.mapping.annotations.Frozen;
//import com.datastax.driver.mapping.annotations.Table;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Table(value = "books")
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

    /**
     * Key: surname
     * Value: name
     */
    @Frozen
    @PrimaryKeyColumn(
            ordinal = 2,
            type = PrimaryKeyType.CLUSTERED
    )
    private Map<String, String> author;
//    private List<String> authorNames;

    @PrimaryKeyColumn(
            ordinal = 3,
            type = PrimaryKeyType.PARTITIONED
    )
    private LocalDate releaseDate;
    private String genre;

    public Book() {
    }

    public Book(UUID id, String title, Map<String, String> author, LocalDate releaseDate, String genre) {
        this.bookId = id;
        this.title = title;
        this.author = author;
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

    public Map<String, String> getAuthor() {
        return author;
    }

    public void setAuthor(Map<String, String> author) {
        this.author = author;
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

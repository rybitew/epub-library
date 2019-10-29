package pl.app.epublibrary.entities;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Table("authors")
public class Author {

    @PrimaryKeyColumn(
            ordinal = 0,
            type = PrimaryKeyType.PARTITIONED
    )
    private UUID author_id;

    @PrimaryKeyColumn(
            ordinal = 1,
            type = PrimaryKeyType.PARTITIONED
    )
    private String authorSurname;

    private String authorName;
    private LocalDate birthDate;

    private List<String> bookTitles;

    public Author(UUID author_id, String authorSurname, String authorName, LocalDate birthDate, List<String> bookTitles) {
        this.author_id = author_id;
        this.authorSurname = authorSurname;
        this.authorName = authorName;
        this.birthDate = birthDate;
        this.bookTitles = bookTitles;
    }

    public UUID getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(UUID author_id) {
        this.author_id = author_id;
    }

    public String getAuthorSurname() {
        return authorSurname;
    }

    public void setAuthorSurname(String authorSurname) {
        this.authorSurname = authorSurname;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<String> getBookTitles() {
        return bookTitles;
    }

    public void setBookTitles(List<String> bookTitles) {
        this.bookTitles = bookTitles;
    }
}

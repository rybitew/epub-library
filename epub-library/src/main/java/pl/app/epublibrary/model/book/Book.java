package pl.app.epublibrary.model.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "books")
public class Book {

    @Id
    @PrimaryKeyColumn(
            type = PrimaryKeyType.PARTITIONED
    )
    private UUID id;

    private String title;

    private Map<UUID, String> authors;

    @Column("release_date")
    private LocalDate releaseDate;

    private String publisher;

    @Column("cover_url")
    private String coverUrl;

    /**
     * Key: user
     * Value: timestamp + comment's text
     */
    private Map<String, String> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(title, book.title) &&
                Objects.equals(authors, book.authors) &&
                Objects.equals(releaseDate, book.releaseDate) &&
                Objects.equals(publisher, book.publisher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, authors, releaseDate, publisher);
    }
}

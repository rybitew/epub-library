package pl.app.epublibrary.model.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "books_by_author_and_publisher")
public class BookByAuthorPublisher {

    @PrimaryKeyColumn(
            type = PrimaryKeyType.PARTITIONED
    )
    private String authors;

    @PrimaryKeyColumn(
            type = PrimaryKeyType.CLUSTERED,
            ordinal = 0
    )
    private String publisher;

    @PrimaryKeyColumn(
            type = PrimaryKeyType.CLUSTERED,
            ordinal = 1,
            name = "book_id"
    )
    private UUID bookId;

    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookByAuthorPublisher that = (BookByAuthorPublisher) o;
        return Objects.equals(authors.trim().toLowerCase(), that.authors.trim().toLowerCase()) &&
                Objects.equals(publisher.trim().toLowerCase(), that.publisher.trim().toLowerCase()) &&
                Objects.equals(bookId, that.bookId) &&
                Objects.equals(title.trim().toLowerCase(), that.title.trim().toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(authors, publisher, bookId, title);
    }
}

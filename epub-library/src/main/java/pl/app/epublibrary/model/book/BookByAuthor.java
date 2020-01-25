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
@Table(value = "books_by_author")
public class BookByAuthor {

    @PrimaryKeyColumn(
            type = PrimaryKeyType.PARTITIONED,
            name = "author"
    )
    private String authors;

    @PrimaryKeyColumn(
            type = PrimaryKeyType.CLUSTERED,
            ordinal = 0
    )
    private String title;

    @PrimaryKeyColumn(
            type = PrimaryKeyType.CLUSTERED,
            ordinal = 1
    )
    private String publisher;

    @PrimaryKeyColumn(
            type = PrimaryKeyType.CLUSTERED,
            ordinal = 2,
            name = "book_id"
    )
    private UUID bookId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookByAuthor that = (BookByAuthor) o;
        return Objects.equals(authors.toLowerCase(), that.authors.toLowerCase()) &&
                Objects.equals(publisher.toLowerCase(), that.publisher.toLowerCase()) &&
                Objects.equals(bookId, that.bookId) &&
                Objects.equals(title.toLowerCase(), that.title.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(authors, publisher, bookId, title);
    }
}

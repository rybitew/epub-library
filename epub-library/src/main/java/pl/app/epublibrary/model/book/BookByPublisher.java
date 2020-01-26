package pl.app.epublibrary.model.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "books_by_publisher")
public class BookByPublisher {

    @PrimaryKeyColumn(
            type = PrimaryKeyType.PARTITIONED,
            name = "publisher_name"
    )
    private String publisherName;

    @PrimaryKeyColumn(
            type = PrimaryKeyType.CLUSTERED,
            ordinal = 0
    )
    private String title;

    @PrimaryKeyColumn(
            type = PrimaryKeyType.CLUSTERED,
            ordinal = 1,
            name = "book_id"
    )
    private UUID bookId;

    private List<String> authors;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookByPublisher that = (BookByPublisher) o;
        return Objects.equals(title.trim().toLowerCase(), that.title.trim().toLowerCase()) &&
                Objects.equals(publisherName.trim().toLowerCase(), that.publisherName.trim().toLowerCase()) &&
                Objects.equals(bookId, that.bookId) &&
                Objects.equals(authors.stream().map(author -> author.trim().toLowerCase()).collect(Collectors.toList()),
                        that.authors.stream().map(author -> author.trim().toLowerCase()).collect(Collectors.toList()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, publisherName, bookId, authors);
    }
}

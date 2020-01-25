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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "book_by_user_library")
public class BookByUserLibrary {

    @PrimaryKeyColumn(
            ordinal = 0,
            type = PrimaryKeyType.PARTITIONED
    )
    private String username;

    @PrimaryKeyColumn(
            ordinal = 1,
            type = PrimaryKeyType.CLUSTERED,
            name = "book_id"
    )
    private UUID bookId;

    private String title;

    private List<String> authors;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookByUserLibrary that = (BookByUserLibrary) o;
        return Objects.equals(username.toLowerCase(), that.username.toLowerCase()) &&
                Objects.equals(bookId, that.bookId) &&
                Objects.equals(title.toLowerCase(), that.title.toLowerCase()) &&
                Objects.equals(authors, that.authors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, bookId, title, authors);
    }
}

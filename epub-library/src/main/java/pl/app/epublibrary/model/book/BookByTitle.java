package pl.app.epublibrary.model.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "books_by_title")
public class BookByTitle {

    @PrimaryKeyColumn(
            type = PrimaryKeyType.PARTITIONED,
            name = "title"
    )
    private String title;

    @PrimaryKeyColumn(
            type = PrimaryKeyType.CLUSTERED,
            ordinal = 0
    )
    private String publisher;

    @PrimaryKeyColumn(
            type = PrimaryKeyType.CLUSTERED,
            name = "book_id",
            ordinal = 1
    )
    private UUID bookId;

    private List<String> authors;
}

package pl.app.epublibrary.model.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

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
    private String author;
    @PrimaryKeyColumn(
            type = PrimaryKeyType.CLUSTERED,
            name = "book_id"
    )
    private UUID bookId;

    @Indexed
    private String title;
}

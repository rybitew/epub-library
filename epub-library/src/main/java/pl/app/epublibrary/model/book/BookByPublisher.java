package pl.app.epublibrary.model.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

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
            name = "book_id"
    )
    private UUID bookId;

    private String title;
}

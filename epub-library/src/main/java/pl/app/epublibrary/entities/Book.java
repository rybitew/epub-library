package pl.app.epublibrary.entities;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table
public class Book {
    @PrimaryKeyColumn(
            name="book_id",
            ordinal=1,
            type= PrimaryKeyType.CLUSTERED,
            ordering = Ordering.DESCENDING)
    private UUID bookId;
    @PrimaryKeyColumn(
            name="author_id",
            ordinal=1,
            type= PrimaryKeyType.CLUSTERED,
            ordering = Ordering.DESCENDING)
    private UUID authorId
}

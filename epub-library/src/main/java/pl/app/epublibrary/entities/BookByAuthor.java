package pl.app.epublibrary.entities;

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
@Table(value = "books_by_author")
public class BookByAuthor {

    @PrimaryKeyColumn(
            ordinal = 0,
            type = PrimaryKeyType.PARTITIONED,
            name = "books_by_author_id"
    )
    private UUID id;

    @PrimaryKeyColumn(
            ordinal = 1,
            type = PrimaryKeyType.PARTITIONED,
            name = "author_surname"
    )
    private String surname;

    @PrimaryKeyColumn(
            ordinal = 1,
            type = PrimaryKeyType.PARTITIONED,
            name = "author_name"
    )
    private String name;

    private String title;
}

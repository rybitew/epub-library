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
@Table(value = "books_by_genre")
public class BookByGenre {

    @PrimaryKeyColumn(
            ordinal = 0,
            type = PrimaryKeyType.PARTITIONED,
            name = "books_by_genre_id"
    )
    private UUID id;

    @PrimaryKeyColumn(
            ordinal = 1,
            type = PrimaryKeyType.PARTITIONED
    )
    private String genre;

    private String title;
}

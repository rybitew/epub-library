package pl.app.epublibrary.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "books")
public class Book {

    @PrimaryKeyColumn(
            ordinal = 0,
            type = PrimaryKeyType.PARTITIONED,
            name = "book_id"
    )
    private UUID id;

    @PrimaryKeyColumn(
            ordinal = 1,
            type = PrimaryKeyType.PARTITIONED
    )
    private String title;

    /**
     * Key: surname
     * Value: name
     */
//    private Map<String, String> author;
    @Column
    private List<Author> authors;

    @Column("release_date")
    private LocalDate releaseDate;

    private String genre;

    @Column("cover_url")
    private String coverUrl;

    private Map<String, String> comments;
}

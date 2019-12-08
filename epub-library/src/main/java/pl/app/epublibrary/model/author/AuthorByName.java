package pl.app.epublibrary.model.author;

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
@Table(value = "authors_by_name")
public class AuthorByName {

    @PrimaryKeyColumn(
            type = PrimaryKeyType.PARTITIONED
    )
    private String name;

    private List<String> title;

    @PrimaryKeyColumn(
            type = PrimaryKeyType.CLUSTERED,
            name = "author_id"
    )
    private UUID authorId;
}

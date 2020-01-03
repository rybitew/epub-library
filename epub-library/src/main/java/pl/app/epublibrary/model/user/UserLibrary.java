package pl.app.epublibrary.model.user;

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
@Table(value = "user_library")
public class UserLibrary {

    @PrimaryKeyColumn(
            ordinal = 0,
            type = PrimaryKeyType.PARTITIONED,
            name = "book_id"
    )
    private UUID bookId;

    @PrimaryKeyColumn(
            ordinal = 1,
            type = PrimaryKeyType.CLUSTERED
    )
    private String username;

    private String title;

    private List<String> authors;
}

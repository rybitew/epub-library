package pl.app.epublibrary.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "comments_by_username")
public class CommentByUsername {

    @PrimaryKeyColumn(
            ordinal = 0,
            type = PrimaryKeyType.PARTITIONED,
            name = "comment_id"
    )
    private UUID id;

    @PrimaryKeyColumn(
            ordinal = 1,
            type = PrimaryKeyType.PARTITIONED
    )
    private String username;

    private  String comment;

    private Instant timestamp;
}

package pl.app.epublibrary.model.comment;

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
            type = PrimaryKeyType.PARTITIONED
    )
    private String username;

    @PrimaryKeyColumn(
            type = PrimaryKeyType.CLUSTERED,
            name = "book_id"
    )
    private UUID bookId;

    @PrimaryKeyColumn(
            type = PrimaryKeyType.CLUSTERED
    )
    private Instant timestamp;

    private  String comment;

    public CommentByUsername(Comment comment) {
        this.username = comment.getUsername();
        this.bookId = comment.getBookId();
        this.timestamp = comment.getTimestamp();
        this.comment = comment.getComment();
    }
}

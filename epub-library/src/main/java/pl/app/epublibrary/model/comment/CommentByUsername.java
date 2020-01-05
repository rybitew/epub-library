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
            name = "comment_id"
    )
    private UUID commentId;

    public CommentByUsername(Comment comment) {
        this.username = comment.getUsername();
        this.commentId = comment.getId();
    }
}

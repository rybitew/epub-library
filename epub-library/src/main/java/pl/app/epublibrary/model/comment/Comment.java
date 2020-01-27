package pl.app.epublibrary.model.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import pl.app.epublibrary.dto.CommentDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "comments")
public class Comment {

    @PrimaryKeyColumn(
            type = PrimaryKeyType.PARTITIONED,
            ordinal = 0
    )
    private UUID id;

    private String username;

    @PrimaryKeyColumn(
            type = PrimaryKeyType.CLUSTERED,
            ordinal = 2,
            name = "book_id"
    )
    private UUID bookId;

    @PrimaryKeyColumn(
            type = PrimaryKeyType.CLUSTERED,
            ordinal = 1
    )
    private Instant timestamp;

    private String title;

    private  String comment;

    public Comment(CommentDto commentDto) {
        this.id = commentDto.getId();
        this.bookId = commentDto.getBookId();
        this.username = commentDto.getUsername();
        this.timestamp = LocalDateTime.parse(commentDto.getTimestamp()).atZone(ZoneId.of("Europe/Warsaw")).toInstant();
        this.title = commentDto.getTitle();
    }

    public Comment(Comment comment) {
        this.id = comment.getId();
        this.bookId = comment.getBookId();
        this.username = comment.getUsername();
        this.timestamp = comment.getTimestamp();
        this.title = comment.getTitle();
        this.comment = comment.getComment();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment1 = (Comment) o;
        return Objects.equals(id, comment1.id) &&
                Objects.equals(username.trim(), comment1.username.trim()) &&
                Objects.equals(bookId, comment1.bookId) &&
                Objects.equals(timestamp, comment1.timestamp) &&
                Objects.equals(title.trim().toLowerCase(), comment1.title.trim().toLowerCase()) &&
                Objects.equals(comment, comment1.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, bookId, timestamp, title, comment);
    }
}

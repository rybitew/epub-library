package pl.app.epublibrary.model.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import pl.app.epublibrary.dto.CommentDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
            ordinal = 2
    )
    private UUID bookId;

    @PrimaryKeyColumn(
            type = PrimaryKeyType.CLUSTERED,
            ordinal = 1
    )
    private Instant timestamp;

    private  String comment;

    public Comment(CommentDto commentDto) {
        this.id = commentDto.getId();
        this.bookId = commentDto.getBookId();
        this.username = commentDto.getUsername();
        this.timestamp = LocalDateTime.parse(commentDto.getTimestamp()).atZone(ZoneId.of("Europe/Warsaw")).toInstant();
    }
}

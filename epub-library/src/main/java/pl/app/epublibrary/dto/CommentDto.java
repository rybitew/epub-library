package pl.app.epublibrary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.app.epublibrary.model.comment.Comment;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private UUID id;

    private String username;

    private UUID bookId;

    private String timestamp;

    private String title;

    private String comment;

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.username = comment.getUsername();
        this.bookId = comment.getBookId();
        this.timestamp = LocalDateTime.ofInstant(comment.getTimestamp(), ZoneId.of("Europe/Warsaw")).toString();
        this.comment = comment.getComment();
        this.title = comment.getTitle();
    }
}

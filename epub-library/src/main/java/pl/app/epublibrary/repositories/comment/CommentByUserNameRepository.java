package pl.app.epublibrary.repositories.comment;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.model.comment.CommentByUsername;
import pl.app.epublibrary.model.comment.CommentId;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface CommentByUserNameRepository extends CassandraRepository<CommentByUsername, String> {

    @Query(value = "SELECT comment_id FROM comments_by_username WHERE username = ?0")
    List<CommentId> findAllCommentsByUsername(String username);

    void deleteAllByUsername(String username);
    void deleteByUsernameAndCommentId(String username, UUID commentId);

    void deleteByUsernameAndCommentIdAndTimestamp(String username, UUID id, Instant timestamp);
}

package pl.app.epublibrary.repositories.comment;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.model.comment.Comment;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentRepository extends CassandraRepository<Comment, UUID> {
    Optional<Comment> findCommentById(UUID id);

    void deleteCommentById(UUID id);
}

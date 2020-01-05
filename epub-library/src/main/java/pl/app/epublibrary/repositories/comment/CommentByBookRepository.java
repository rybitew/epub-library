package pl.app.epublibrary.repositories.comment;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.model.comment.CommentByBook;
import pl.app.epublibrary.model.comment.CommentId;

import java.util.Set;
import java.util.UUID;

@Repository
public interface CommentByBookRepository extends CassandraRepository<CommentByBook, UUID> {

    @Query(value = "SELECT comment_id FROM comments_by_book_id WHERE book_id = ?0")
    Set<CommentId> findAllCommentsByBookId(UUID bookId);

    void deleteAllByBookId(UUID bookId);
    void deleteByBookIdAndCommentId(UUID bookId, UUID commentId);
}

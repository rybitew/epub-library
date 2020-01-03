package pl.app.epublibrary.repositories.commnet;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.model.comment.CommentByBook;

import java.util.UUID;

@Repository
public interface CommentByBookRepository extends CassandraRepository<CommentByBook, UUID> {

    void deleteAllByBookId(UUID uuid);

    void deleteByBookIdAndUsername(UUID bookId, String username);
}

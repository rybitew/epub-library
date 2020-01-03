package pl.app.epublibrary.repositories.commnet;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.model.comment.CommentByUsername;

import java.util.UUID;

@Repository
public interface CommentByUserNameRepository extends CassandraRepository<CommentByUsername, String> {

    void deleteAllByUsername(String username);

    void deleteByBookIdAndUsername(UUID bookId, String username);
}

package pl.app.epublibrary.repositories.user;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.model.user.CommentByUsername;

import java.util.UUID;

@Repository
public interface CommentByUserNameRepository extends CassandraRepository<CommentByUsername, UUID> {
}

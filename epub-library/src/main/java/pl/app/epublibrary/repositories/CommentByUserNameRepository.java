package pl.app.epublibrary.repositories;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.entities.CommentByUsername;

import java.util.UUID;

@Repository
public interface CommentByUserNameRepository extends CassandraRepository<CommentByUsername, UUID> {
}

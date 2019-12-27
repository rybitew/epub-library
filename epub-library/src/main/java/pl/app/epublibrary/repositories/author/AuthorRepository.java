package pl.app.epublibrary.repositories.author;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.model.author.Author;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthorRepository extends CassandraRepository<Author, UUID> {

    Optional<Author> getById(UUID uuid);
}

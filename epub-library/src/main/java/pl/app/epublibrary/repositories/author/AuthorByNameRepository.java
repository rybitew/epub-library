package pl.app.epublibrary.repositories.author;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.model.author.AuthorByName;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthorByNameRepository extends CassandraRepository<AuthorByName, String> {

    AuthorByName findByName(String name);

    AuthorByName findByNameAndTitle(String name, String title);

    void deleteByAuthorIdAndAndName(UUID id, String name);
}


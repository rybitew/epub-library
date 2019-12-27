package pl.app.epublibrary.repositories.author;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.model.author.AuthorByName;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuthorByNameRepository extends CassandraRepository<AuthorByName, String> {

    AuthorByName findByNameAndAuthorId(String name, UUID id);

    List<AuthorByName> findAllByName(String name);

    void deleteByAuthorIdAndAndName(UUID id, String name);
}


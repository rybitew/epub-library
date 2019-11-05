package pl.app.epublibrary.repositories;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.entities.BookByGenre;

import java.util.UUID;

@Repository
public interface BookByGenreRepository extends CassandraRepository<BookByGenre, UUID> {
}

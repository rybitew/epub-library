package pl.app.epublibrary.repositories;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.entities.BookByReleaseDate;

import java.util.UUID;

@Repository
public interface BookByReleaseDateRepository extends CassandraRepository<BookByReleaseDate, UUID> {
}

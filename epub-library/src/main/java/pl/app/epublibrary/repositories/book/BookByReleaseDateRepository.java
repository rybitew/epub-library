package pl.app.epublibrary.repositories.book;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.model.book.BookByReleaseDate;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface BookByReleaseDateRepository extends CassandraRepository<BookByReleaseDate, LocalDate> {
    void deleteByBookId(UUID id);
}

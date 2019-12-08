package pl.app.epublibrary.repositories.book;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.model.book.BookByPublisher;

import java.util.UUID;

@Repository
public interface BookByPublisherRepository extends CassandraRepository<BookByPublisher, String> {
    void deleteByBookId(UUID id);
}

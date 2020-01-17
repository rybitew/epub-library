package pl.app.epublibrary.repositories.book;

import org.springframework.data.cassandra.repository.CassandraRepository;
import pl.app.epublibrary.model.book.BookByAuthorPublisher;

import java.util.List;
import java.util.UUID;

public interface BookByAuthorPublisherRepository extends CassandraRepository<BookByAuthorPublisher, UUID> {

    List<BookByAuthorPublisher> findAllByAuthorsAndPublisher(String author, String publisher);

    void deleteByAuthorsAndPublisherAndBookId(String author, String publisher, UUID bookId);
}

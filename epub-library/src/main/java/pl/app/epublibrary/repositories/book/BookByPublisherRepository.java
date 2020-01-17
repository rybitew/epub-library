package pl.app.epublibrary.repositories.book;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.model.book.BookByPublisher;
import pl.app.epublibrary.model.book.BookPublisher;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Repository
public interface BookByPublisherRepository extends CassandraRepository<BookByPublisher, String> {

    List<BookByPublisher> findAllByPublisherName(String publisher);

    List<BookByPublisher> findAllByPublisherNameAndTitle(String publisher, String title);

    void deleteByBookIdAndPublisherNameAndTitle(UUID id, String publisher, String title);

    @Query(value = "SELECT DISTINCT publisher_name FROM books_by_publisher")
    Set<BookPublisher> findAllPublishers();
}

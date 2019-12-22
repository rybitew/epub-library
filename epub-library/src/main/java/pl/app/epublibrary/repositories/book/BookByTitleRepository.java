package pl.app.epublibrary.repositories.book;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.model.book.BookByTitle;

import java.util.UUID;

@Repository
public interface BookByTitleRepository extends CassandraRepository<BookByTitle, String> {
    void deleteByBookIdAndTitle(UUID id, String title);
}

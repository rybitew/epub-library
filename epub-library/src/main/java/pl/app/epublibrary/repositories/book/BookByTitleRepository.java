package pl.app.epublibrary.repositories.book;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.model.book.BookByTitle;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookByTitleRepository extends CassandraRepository<BookByTitle, String> {

    List<BookByTitle> findAllByTitle(String title);

    void deleteByBookIdAndTitleAndPublisher(UUID id, String title, String publisher);
}

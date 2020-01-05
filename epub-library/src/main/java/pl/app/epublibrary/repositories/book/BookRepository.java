package pl.app.epublibrary.repositories.book;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.model.book.Book;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends CassandraRepository<Book, UUID> {

    Book getById(UUID uuid);
}

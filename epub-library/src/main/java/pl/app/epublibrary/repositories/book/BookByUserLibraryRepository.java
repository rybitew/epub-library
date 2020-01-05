package pl.app.epublibrary.repositories.book;

import org.springframework.data.cassandra.repository.CassandraRepository;
import pl.app.epublibrary.model.book.BookByUserLibrary;

import java.util.Set;
import java.util.UUID;

public interface BookByUserLibraryRepository extends CassandraRepository<BookByUserLibrary, String> {

    Set<BookByUserLibrary> findAllByUsername(String username);

    void deleteByUsernameAndBookId(String username, UUID bookId);
}

package pl.app.epublibrary.repositories.book;

import org.springframework.data.cassandra.repository.CassandraRepository;
import pl.app.epublibrary.model.book.UserLibraryByBook;

import java.util.Set;
import java.util.UUID;

public interface UserLibraryByBookRepository extends CassandraRepository<UserLibraryByBook, UUID> {

    Set<UserLibraryByBook> findAllByBookId(UUID id);

    UserLibraryByBook findByBookIdAndUsername(UUID id, String username);

    void deleteByUsernameAndBookId(String username, UUID bookId);
}

package pl.app.epublibrary.repositories.book;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.model.book.BookAuthor;
import pl.app.epublibrary.model.book.BookByAuthor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface BookByAuthorRepository extends CassandraRepository<BookByAuthor, String> {

    BookByAuthor findByAuthorAndBookId(String author, UUID bookId);

    BookByAuthor findByAuthorAndTitle(String author, String title);

    void deleteByBookIdAndAuthor(UUID id, String author);

    List<BookByAuthor> findAllByAuthor(String author);

    @Query(value = "SELECT DISTINCT author FROM books_by_author")
    Set<BookAuthor> findAllAuthors();
}

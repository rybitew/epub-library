package pl.app.epublibrary.repositories.book;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.model.book.BookAuthor;
import pl.app.epublibrary.model.book.BookByAuthor;

import java.util.List;
import java.util.Set;

@Repository
public interface BookByAuthorRepository extends CassandraRepository<BookByAuthor, String> {

    BookByAuthor findByAuthorsAndTitle(String author, String title);

    BookByAuthor findByAuthorsAndPublisherAndTitle(String author, String publisher, String title);

    void deleteByTitleAndAuthors(String title, String author);

    List<BookByAuthor> findAllByAuthors(String author);

    @Query(value = "SELECT DISTINCT author FROM books_by_author")
    Set<BookAuthor> findAllAuthors();
}

package pl.app.epublibrary.repositories;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.entities.Book;

import java.util.UUID;

@Repository
public interface BookRepository extends CassandraRepository<Book, UUID> {

}

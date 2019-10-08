package pl.app.epublibrary.repositories;

import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.entities.Book;

@Repository
public interface BookRepository extends CassandraRepository<Book, MapId> {
    //
}
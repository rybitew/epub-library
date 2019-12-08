package pl.app.epublibrary.repositories.publisher;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.model.publisher.Publisher;

import java.util.UUID;

@Repository
public interface PublisherRepository extends CassandraRepository<Publisher, UUID> {
}

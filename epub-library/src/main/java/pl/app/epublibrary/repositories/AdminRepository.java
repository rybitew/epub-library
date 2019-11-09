package pl.app.epublibrary.repositories;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.entities.Admin;

import java.util.UUID;

@Repository
public interface AdminRepository extends CassandraRepository<Admin, UUID> {
}

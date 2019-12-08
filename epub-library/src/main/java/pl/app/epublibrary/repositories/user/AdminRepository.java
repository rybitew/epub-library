package pl.app.epublibrary.repositories.user;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.model.user.Admin;

import java.util.UUID;

@Repository
public interface AdminRepository extends CassandraRepository<Admin, UUID> {
}

package pl.app.epublibrary.repositories.user;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.model.user.UserRole;

@Repository
public interface UserRoleRepository extends CassandraRepository<UserRole, String> {
}

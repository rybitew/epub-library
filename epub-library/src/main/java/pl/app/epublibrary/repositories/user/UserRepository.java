package pl.app.epublibrary.repositories.user;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import pl.app.epublibrary.model.user.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CassandraRepository<User, UUID> {

    User findByUsernameAndEmail(String username, String email);

    User findByUsername(String username);

    User findByEmail(String email);

    @Query(value = "SELECT elevated FROM users WHERE username = ?0")
    Boolean findIfElevated(String username);

    void deleteByUsername(String username);
}

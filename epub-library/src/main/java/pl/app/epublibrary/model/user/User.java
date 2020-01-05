package pl.app.epublibrary.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.validation.constraints.Email;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "users")
public class User {

    @PrimaryKeyColumn(
            type = PrimaryKeyType.PARTITIONED
    )
    private String username;

    private String password;

    @Email
    @PrimaryKeyColumn(
            type = PrimaryKeyType.CLUSTERED
    )
    private String email;
}

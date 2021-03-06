package pl.app.epublibrary.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Objects;

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

    @Indexed
    private String email;

    private Boolean elevated;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username.toLowerCase(), user.username.toLowerCase()) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email) &&
                Objects.equals(elevated, user.elevated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, email, elevated);
    }
}

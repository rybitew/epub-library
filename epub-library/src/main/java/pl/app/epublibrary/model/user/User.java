package pl.app.epublibrary.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

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
}

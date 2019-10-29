package pl.app.epublibrary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration{

    private final String KEYSPACE = "librarydb";

    @Bean
    public CassandraClusterFactoryBean cluster() {

        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setContactPoints("localhost");
        cluster.setUsername("admin");
        cluster.setPassword("admin");

        return cluster;
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        CreateKeyspaceSpecification specification = CreateKeyspaceSpecification.createKeyspace(KEYSPACE)
                .with(KeyspaceOption.DURABLE_WRITES, true);
        return Arrays.asList(specification);
    }

//    @Override
//    protected List<DropKeyspaceSpecification> getKeyspaceDrops() {
//        return Arrays.asList(DropKeyspaceSpecification.dropKeyspace((KEYSPACE)));
//    }

    @Override
    protected String getKeyspaceName() {
        return KEYSPACE;
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[] {"pl.app.epublibrary.entities"};
    }
}
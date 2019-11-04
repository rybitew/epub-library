package pl.app.epublibrary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraCqlClusterFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableCassandraRepositories(basePackages = "pl.app.epublibrary.repositories")
public class CassandraConfig extends AbstractCassandraConfiguration{

    private final String KEYSPACE = "librarydb";
    private final String CONTACT_POINTS = "localhost";
    private final String USERNAME = "admin";
    private final String PASSWORD = "admin";

    @Bean
    @Override
    public CassandraCqlClusterFactoryBean cluster() {
        CassandraCqlClusterFactoryBean bean = new CassandraCqlClusterFactoryBean();
        bean.setKeyspaceCreations(getKeyspaceCreations());
        bean.setContactPoints(CONTACT_POINTS);
        bean.setUsername(USERNAME);
        bean.setPassword(PASSWORD);
        return bean;
    }
    /*
     * Provide a keyspace name to the configuration.
     */
    @Override
    public String getKeyspaceName() {
        return KEYSPACE;
    }

    /*
     * Automatically creates a Keyspace if it doesn't exist
     */
    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        CreateKeyspaceSpecification specification = CreateKeyspaceSpecification
                .createKeyspace(KEYSPACE).ifNotExists()
                .with(KeyspaceOption.DURABLE_WRITES, true).withSimpleReplication();
        return Arrays.asList(specification);
    }


    /*
     * Automatically configure a table if doesn't exist
     */
    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }


    /*
     * Get the entity package (where the entity class has the @Table annotation)
     */
    @Override
    public String[] getEntityBasePackages() {
        return new String[] { "pl.app.epublibrary.entities" };
    }
//    @Bean
//    public CassandraClusterFactoryBean cluster() {
//
//        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
//        cluster.setContactPoints("localhost");
//        cluster.setUsername("admin");
//        cluster.setPassword("admin");
//
//        return cluster;
//    }
//
//    @Override
//    public SchemaAction getSchemaAction() {
//        return SchemaAction.CREATE_IF_NOT_EXISTS;
//    }
//
//    @Override
//    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
//        CreateKeyspaceSpecification specification = CreateKeyspaceSpecification.createKeyspace(KEYSPACE)
//                .ifNotExists()
//                .withSimpleReplication(3)
//                .with(KeyspaceOption.DURABLE_WRITES, true);
//        List<CreateKeyspaceSpecification> specifications = new ArrayList<>();
//        specifications.add(specification);
//        return specifications;
//    }
//
//    @Override
//    protected List<DropKeyspaceSpecification> getKeyspaceDrops() {
//        return Arrays.asList(DropKeyspaceSpecification.dropKeyspace((KEYSPACE)));
//    }
//
//    @Override
//    protected String getKeyspaceName() {
//        return KEYSPACE;
//    }
//
//    @Override
//    public String[] getEntityBasePackages() {
//        return new String[] {"pl.app.epublibrary.entities"};
//    }
}
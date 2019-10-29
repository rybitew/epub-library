package pl.app.epublibrary;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;

@SpringBootApplication
public class EpubLibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(EpubLibraryApplication.class, args);
	}

}

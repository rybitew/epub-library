package pl.app.epublibrary;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class EpubLibraryApplication {

	@BeforeClass
	public static void startCassandraEmbedded() {
		try {
			EmbeddedCassandraServerHelper.startEmbeddedCassandra();
		} catch (TTransportException | InterruptedException | IOException e) {
			e.printStackTrace();
		}
		Cluster cluster = Cluster.builder()
				.addContactPoints("127.0.0.1").withPort(9142).build();
		Session session = cluster.connect();
	}
	@AfterClass
	public static void stopCassandraEmbedded() {
		EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
	}
	public static void main(String[] args) {
		SpringApplication.run(EpubLibraryApplication.class, args);
	}

}

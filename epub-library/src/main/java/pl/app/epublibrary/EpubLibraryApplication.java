package pl.app.epublibrary;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import pl.app.epublibrary.entities.Book;
import pl.app.epublibrary.services.BookService;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootApplication
public class EpubLibraryApplication {

	private static BookService bookService;

	@Autowired
	public EpubLibraryApplication(BookService bookService) {
		this.bookService = bookService;
	}

	public static void main(String[] args) {
		SpringApplication.run(EpubLibraryApplication.class, args);

		Book book = new Book();
		book.setBookId(UUID.randomUUID());
		Map<String, String> authors = new HashMap<>();
		authors.put("Kowalski", "Jan");
		book.setTitle("Historia");
		book.setAuthor(authors);
		book.setGenre("Historical");
		book.setReleaseDate(LocalDate.of(2000, 10, 10));
		bookService.saveEntity(book);
	}

}

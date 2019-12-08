package pl.app.epublibrary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.app.epublibrary.model.book.Book;
import pl.app.epublibrary.repositories.book.BookRepository;
import pl.app.epublibrary.services.book.BookService;

import java.time.LocalDate;
import java.util.*;

@SpringBootApplication
public class EpubLibraryApplication {

	private static BookService bookService;

	@Autowired
	public EpubLibraryApplication(BookService bookService, BookRepository bookRepository) {
		this.bookService = bookService;
	}

	public static void main(String[] args) {
		SpringApplication.run(EpubLibraryApplication.class, args);

		Book book = new Book();
		book.setId(UUID.randomUUID());
		Map<UUID, String> authors1  = new HashMap<>();
		authors1.put(UUID.randomUUID(), "Jim Jimmy");
		book.setTitle("Chronicles");
		book.setAuthors(authors1);
		book.setReleaseDate(LocalDate.of(2000, 10, 10));
		bookService.saveBook(book);

		Book book1 = new Book();
		book1.setId(UUID.randomUUID());
		Map<UUID, String> authors2  = new HashMap<>();
		UUID uuid1 = UUID.randomUUID();
		authors2.put(uuid1, "Jim Tony");
		authors2.put(UUID.randomUUID(),"Tom Tony");
		authors2.put(UUID.randomUUID(), "Tom Wllk");
		book1.setTitle("Undertitle");
		book1.setAuthors(authors2);
		book1.setReleaseDate(LocalDate.of(2011, 12, 10));
		bookService.saveBook(book1);

		Book book2 = new Book();
		book2.setId(UUID.randomUUID());
		Map<UUID, String> authors3  = new HashMap<>();
		authors3.put(uuid1, "Jim Tony");
		authors3.put(UUID.randomUUID(), "Tom Ade");
		book2.setTitle("Tok n");
		book2.setAuthors(authors3);
		book2.setReleaseDate(LocalDate.of(1990, 11, 20));
		bookService.saveBook(book2);
	}

}

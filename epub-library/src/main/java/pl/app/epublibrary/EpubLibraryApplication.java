package pl.app.epublibrary;

import nl.siegmann.epublib.domain.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.app.epublibrary.model.book.Book;
import pl.app.epublibrary.repositories.book.BookByAuthorRepository;
import pl.app.epublibrary.services.BookService;
import pl.app.epublibrary.util.MetadataReader;

import java.time.LocalDate;
import java.util.*;

@SpringBootApplication
public class EpubLibraryApplication {

	private static BookService bookService;
	private static MetadataReader metadataReader;
 	private static BookByAuthorRepository bookByAuthorRepository;

	@Autowired
	public EpubLibraryApplication(BookService bookService, BookByAuthorRepository bookRepository, MetadataReader metadataReader) {
		this.bookService = bookService;
		this.bookByAuthorRepository = bookRepository;
		this.metadataReader = metadataReader;
	}

	public static void main(String[] args) {
		SpringApplication.run(EpubLibraryApplication.class, args);

		Book book = new Book();
		book.setId(UUID.randomUUID());
		List<String> authors1  = new LinkedList<>();
		authors1.add("Te Bee");
		book.setTitle("Chronicles");
		book.setAuthors(authors1);
		book.setReleaseDate(LocalDate.of(1999, 4, 10));
		bookService.saveBook(book);

		Book book1 = new Book();
		book1.setId(UUID.randomUUID());
		List<String> authors2  = new LinkedList<>();
		UUID uuid1 = UUID.randomUUID();
		authors2.add("Jim Tony");
		authors2.add("Tom Tony");
		authors2.add("Tom Wllk");
		book1.setTitle("Undertitle");
		book1.setAuthors(authors2);
		book1.setReleaseDate(LocalDate.of(2011, 12, 10));
		bookService.saveBook(book1);

		Book book2 = new Book();
		book2.setId(UUID.randomUUID());
		List<String> authors3  = new LinkedList<>();
		authors3.add("Jim Tony");
		authors3.add("Tom Ade");
		book2.setTitle("Tok n");
		book2.setAuthors(authors3);
		book2.setReleaseDate(LocalDate.of(1990, 11, 20));
		bookService.saveBook(book2);

		System.out.println("done");
		metadataReader.setBook("boska-komedia.epub");
		LocalDate date = metadataReader.getReleaseDate();
		Resource res = metadataReader.getCoverImage();
	}

}

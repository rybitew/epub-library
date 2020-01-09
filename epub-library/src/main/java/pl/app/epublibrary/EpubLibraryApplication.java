package pl.app.epublibrary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.app.epublibrary.config.FileStorageProperties;
import pl.app.epublibrary.model.book.Book;
import pl.app.epublibrary.repositories.book.BookByAuthorRepository;
import pl.app.epublibrary.services.BookService;
import pl.app.epublibrary.services.CommentService;
import pl.app.epublibrary.services.UserService;
import pl.app.epublibrary.util.MetadataReader;

import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.*;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class})
public class EpubLibraryApplication {

	private static BookService bookService;
	private static UserService userService;
	private static CommentService commentService;
	private static MetadataReader metadataReader;
 	private static BookByAuthorRepository bookByAuthorRepository;

	@Autowired
	public EpubLibraryApplication(BookService bookService, BookByAuthorRepository bookRepository,
								  MetadataReader metadataReader, UserService userService, CommentService commentService) {
		this.bookService = bookService;
		this.bookByAuthorRepository = bookRepository;
		this.metadataReader = metadataReader;
		this.userService = userService;
		this.commentService = commentService;
	}


	public static void main(String[] args) {
		SpringApplication.run(EpubLibraryApplication.class, args);
		try {
			Book book = new Book();
			book.setId(UUID.randomUUID());
			List<String> authors1 = new LinkedList<>();
			authors1.add("Te Bee");
			book.setTitle("Chronicles");
			book.setAuthors(authors1);
			book.setReleaseDate(LocalDate.of(1999, 4, 10));
			book.setPublisher("Wyd1");
			bookService.saveBook(book);

			Book book1 = new Book();
			book1.setId(UUID.randomUUID());
			List<String> authors2 = new LinkedList<>();
			UUID uuid1 = UUID.randomUUID();
			authors2.add("Jim Tony");
			authors2.add("Tom Tony");
			authors2.add("Tom Wllk");
			book1.setTitle("Undertitle");
			book1.setAuthors(authors2);
			book1.setReleaseDate(LocalDate.of(2011, 12, 10));
			book1.setPublisher("Wyd1");
			bookService.saveBook(book1);

			Book book2 = new Book();
			book2.setId(UUID.randomUUID());
			List<String> authors3 = new LinkedList<>();
			authors3.add("Jim Tony");
			authors3.add("Tom Ade");
			book2.setTitle("Tok n");
			book2.setAuthors(authors3);
			book2.setReleaseDate(LocalDate.of(1990, 11, 20));
			book2.setPublisher("Wyd2");
			bookService.saveBook(book2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("inserting done");
//		try {
//			userService.deleteFromUserLibrary("xz", UUID.fromString("5f3bcc80-98c9-4c72-82c5-0d1c243643e6"));
//		} catch (UnexpectedErrorException e) {
//			e.printStackTrace();
//		}
//		bookService.updateAuthor(UUID.fromString("d062f741-3216-4aeb-8949-d1b851d71aa2"), new LinkedList<>(List.of("bu1", "bu2")));
//		try {
//			bookService.deleteBook(UUID.fromString("5f3bcc80-98c9-4c72-82c5-0d1c243643e6"));
//		} catch (InvalidBookIdException e) {
//			e.printStackTrace();
//		}
		System.out.println("done");
	}


}

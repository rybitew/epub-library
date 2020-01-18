package pl.app.epublibrary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.app.epublibrary.config.FileStorageProperties;
import pl.app.epublibrary.exceptions.InvalidEmailException;
import pl.app.epublibrary.exceptions.InvalidEmailFormatException;
import pl.app.epublibrary.exceptions.InvalidUsernameException;
import pl.app.epublibrary.model.book.Book;
import pl.app.epublibrary.model.user.User;
import pl.app.epublibrary.services.BookService;
import pl.app.epublibrary.services.UserService;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class})
public class EpubLibraryApplication {

    private static BookService bookService;
    private static UserService userService;

    @Autowired
    public EpubLibraryApplication(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }


    public static void main(String[] args) {
        SpringApplication.run(EpubLibraryApplication.class, args);
        try {
            if (userService.findUserByUsername("admin") == null)
                userService.saveUser(new User("admin", "admin", "admin@admin.com", true));
        } catch (InvalidEmailException | InvalidUsernameException | InvalidEmailFormatException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    }


}

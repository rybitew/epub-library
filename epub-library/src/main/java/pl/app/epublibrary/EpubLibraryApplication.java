package pl.app.epublibrary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.app.epublibrary.config.FileStorageProperties;
import pl.app.epublibrary.exceptions.InvalidEmailException;
import pl.app.epublibrary.exceptions.InvalidEmailFormatException;
import pl.app.epublibrary.exceptions.InvalidUsernameException;
import pl.app.epublibrary.model.user.User;
import pl.app.epublibrary.services.UserService;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class})
public class EpubLibraryApplication {

	private static UserService userService;

    @Autowired
    public EpubLibraryApplication(UserService userService) {
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
    }


}

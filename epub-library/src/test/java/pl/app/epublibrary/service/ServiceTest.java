package pl.app.epublibrary.service;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.app.epublibrary.exceptions.*;
import pl.app.epublibrary.model.user.User;
import pl.app.epublibrary.repositories.user.UserRepository;
import pl.app.epublibrary.services.UserService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ServiceTest {

    @MockBean(name = "userRepository")
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void saveUserTestInvalid() throws InvalidUsernameException, InvalidEmailException {
        when(userRepository.findByUsername("test")).thenReturn(new User());
        when(userRepository.findByEmail("test@test.test")).thenReturn(new User());
        assertThrows(InvalidEmailFormatException.class,
                () -> userService.saveUser(new User("test", "test", "test", false)));
        assertThrows(InvalidEmailFormatException.class,
                () -> userService.saveUser(new User("test", "test@", "test", false)));
        assertThrows(InvalidEmailFormatException.class,
                () -> userService.saveUser(new User("test", "@test.test", "test", false)));
        assertThrows(InvalidEmailFormatException.class,
                () -> userService.saveUser(new User("test", "test@test", "test", false)));
        assertThrows(InvalidEmailFormatException.class,
                () -> userService.saveUser(new User("test", ".test", "test", false)));

        assertThrows(InvalidUsernameException.class,
                () -> userService.saveUser(new User(null, "test", "test", false)));

        assertThrows(InvalidUsernameException.class,
                () -> userService.saveUser(null));

        when(userService.findUserByUsername("test")).thenReturn(null);
        when(userService.findUserByEmail("test@test.test")).thenReturn(new User());
        User user = new User("test", "test", "test@test.test", false);
        assertThrows(InvalidEmailException.class,
                () -> userService.saveUser(user));

        when(userService.findUserByUsername("test")).thenReturn(new User());
        when(userService.findUserByEmail("test@test.test")).thenReturn(null);
        assertThrows(InvalidUsernameException.class,
                () -> userService.saveUser(user));
    }

    @Test
    void saveUserTestValid()
            throws InvalidUsernameException, InvalidEmailFormatException, InvalidEmailException, UserNotFoundException {
        when(userRepository.findByUsername("test")).thenReturn(new User());
        when(userRepository.findByEmail("test@test.test")).thenReturn(new User());
        when(userService.findUserByUsername("test")).thenReturn(null);
        when(userService.findUserByEmail("test@test.test")).thenReturn(null);
        userService.saveUser(new User("test", "test", "test@test.test", null));
        userService.saveUser(new User("test", "test", "test@test.test", true));

        userService.saveUser(new User("test", "test", "test.t@test.test", true));
        userService.saveUser(new User("test", "test", "test21@test.test", true));
        userService.saveUser(new User("test", "test", "test21@test2.test", true));
        userService.saveUser(new User("test", "test", "te.s.t@test2.test", true));
    }

    @Test
    void deleteUserTestInvalid() {
        assertThrows(InvalidUsernameException.class,
                () -> userService.deleteUser(null));
    }

    @Test
    void deleteUserTestValid() throws InvalidUsernameOrBookIdException, InvalidUsernameException {
        userService.deleteUser("test");
    }

    @Test
    void elevateUserInvalid() {
        assertThrows(InvalidUsernameException.class,
                () -> userService.elevateUser(null));
    }

    @Test
    void elevateUserValid() throws InvalidUsernameException {
        when(userRepository.findByUsername("test")).thenReturn(new User());
        when(userRepository.findByEmail("test")).thenReturn(new User());
        userService.elevateUser("test");

        userService.elevateUser("not found");
    }
}

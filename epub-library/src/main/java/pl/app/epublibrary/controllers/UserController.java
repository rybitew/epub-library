package pl.app.epublibrary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.app.epublibrary.dto.UserDto;
import pl.app.epublibrary.exception.*;
import pl.app.epublibrary.model.book.BookByUserLibrary;
import pl.app.epublibrary.model.user.User;
import pl.app.epublibrary.services.UserService;

import java.util.Set;
import java.util.UUID;

@RestController
public class UserController {

    private UserService userService;

    private enum roles {
        USER(0), ADMIN(1);

        private final int value;

        roles(int value) {
            this.value = value;
        }

        int getValue() {
            return value;
        }
    }


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/user/register")
    public boolean addUser(@RequestBody User user) {
        try {
            if (user != null) {
                user.setElevated(false);
                userService.saveUser(user);
                return true;
            }
            throw new InvalidUsernameException();
        } catch (InvalidEmailException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "User with given email already exists.", e);
        } catch (InvalidUsernameException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "User with given username already exists.", e);
        } catch (InvalidEmailFormatException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Invalid e-mail format.", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error.", e);
        }
    }

    @PostMapping(value = "user/elevate/")
    public void elevateUser(@RequestBody String username) {
        try {
            userService.elevateUser(username);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error.", e);
        }
    }

    @GetMapping(value = "user/elevated/", params = {"username"})
    public boolean isElevated(@RequestParam(value = "username") String username) {
        try {
            return userService.isUserElevated(username);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error.", e);
        }
    }


    @PostMapping(value = "user/login")
    public int login(@RequestBody UserDto userDto) {
        try {
            User user = userService.findUserByUsername(userDto.getUsername());
            if (user != null && user.getPassword().equals(userDto.getPassword())) {
                return user.isElevated() ? roles.ADMIN.value : roles.USER.value;
            }
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid username or password.");
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid username or password.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error.", e);
        }
    }

    @DeleteMapping(value = "/user/delete/", params = {"username"})
    public void deleteUser(@RequestParam(name = "username") String username) {
        try {
            userService.deleteUser(username);
        } catch (InvalidUsernameOrBookIdException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User does not exist.", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error.", e);
        }
    }

    @DeleteMapping(value = "user/library/delete/", params = {"id", "username"})
    public void deleteBookFromLibrary(@RequestParam(value = "id") String bookId,
                                      @RequestParam(value = "username") String username) {
        try {
            userService.deleteFromUserLibrary(username, UUID.fromString(bookId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error.", e);
        }
    }

    @GetMapping(value = "/user/", params = {"username"})
    public @ResponseBody
    User getUserByUsername(@RequestParam(value = "username") String username) {
        try {
            User user = userService.findUserByUsername(username);
            if (user != null) {
                user.setPassword(null);
                return user;
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error.", e);
        }
    }

    @GetMapping(value = "/user/library/", params = {"username"})
    public @ResponseBody
    Set<BookByUserLibrary> getUserLibrary(@RequestParam(value = "username") String username) {
        try {
            return userService.findAllUserLibraryBooks(username);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error.", e);
        }
    }
}

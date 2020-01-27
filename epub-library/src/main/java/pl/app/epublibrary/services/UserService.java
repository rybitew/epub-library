package pl.app.epublibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.app.epublibrary.exceptions.*;
import pl.app.epublibrary.model.book.BookByUserLibrary;
import pl.app.epublibrary.model.user.User;
import pl.app.epublibrary.repositories.book.BookByUserLibraryRepository;
import pl.app.epublibrary.repositories.book.UserLibraryByBookRepository;
import pl.app.epublibrary.repositories.user.UserRepository;

import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;
    private CommentService commentService;
    private BookByUserLibraryRepository bookByUserLibraryRepository;
    private UserLibraryByBookRepository userLibraryByBookRepository;

    private static final String EMAIL_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    @Autowired
    public UserService(UserRepository userRepository, CommentService commentService,
                       BookByUserLibraryRepository bookByUserLibraryRepository,
                       UserLibraryByBookRepository userLibraryByBookRepository) {
        this.userRepository = userRepository;
        this.commentService = commentService;
        this.bookByUserLibraryRepository = bookByUserLibraryRepository;
        this.userLibraryByBookRepository = userLibraryByBookRepository;
    }

    //region CRUD
    public void saveUser(User user)
            throws InvalidEmailException, InvalidUsernameException, InvalidEmailFormatException, InvalidPasswordException {
        if (user == null || user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new InvalidUsernameException();
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty() || !user.getEmail().matches(EMAIL_PATTERN)) {
            throw new InvalidEmailFormatException();
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new InvalidPasswordException();
        }
        user.setUsername(user.getUsername().trim());
        user.setEmail(user.getEmail().trim());
        user.setPassword(user.getPassword().trim());
        user.setUsername(user.getUsername().toLowerCase());
        user.setEmail(user.getEmail().toLowerCase());
        if (findUserByUsername(user.getUsername()) == null) {
            if (findUserByEmail(user.getEmail()) == null) {
                userRepository.save(user);
            } else {
                throw new InvalidEmailException();
            }
        } else {
            throw new InvalidUsernameException();
        }
    }

    public void deleteUser(String username) throws InvalidUsernameOrBookIdException, InvalidUsernameException {
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidUsernameException();
        }
        username = username.trim();
        userRepository.deleteByUsername(username);
        Set<BookByUserLibrary> books = bookByUserLibraryRepository.findAllByUsername(username);
        for (BookByUserLibrary book : books) {
            try {
                deleteFromUserLibrary(book.getUsername(), book.getBookId());
            } catch (UnexpectedErrorException e) {
                throw new InvalidUsernameOrBookIdException();
            }
        }
        commentService.deleteAllUserComments(username);

    }

    public void elevateUser(String username) throws InvalidUsernameException {
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidUsernameException();
        }
        username = username.trim();

        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setElevated(true);
            userRepository.save(user);
        }
    }
//endregion

//region ENDPOINT

    public Boolean isUserElevated(String username) throws InvalidUsernameException {
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidUsernameException();
        }
        username = username.trim().toLowerCase();

        Boolean isElevated = userRepository.findIfElevated(username);
        return isElevated == null? false : isElevated;
    }

    public User findUserByUsername(String username) throws InvalidUsernameException {
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidUsernameException();
        }
        username = username.trim();
        return userRepository.findByUsername(username);
    }

    public User findUserByEmail(String email) throws InvalidEmailException {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidEmailException();
        }
        email = email.trim().toLowerCase();
        return userRepository.findByEmail(email);
    }

    public void deleteFromUserLibrary(String username, UUID bookId)
            throws UnexpectedErrorException, InvalidUsernameOrBookIdException {
        if (username == null || username.trim().isEmpty() || bookId == null) {
            throw new InvalidUsernameOrBookIdException();
        }
        username = username.trim().toLowerCase();
        try {
            bookByUserLibraryRepository.deleteByUsernameAndBookId(username, bookId);
            userLibraryByBookRepository.deleteByUsernameAndBookId(username, bookId);
        } catch (Exception e) {
            throw new UnexpectedErrorException();
        }
    }

    public Set<BookByUserLibrary> findAllUserLibraryBooks(String username) throws InvalidUsernameException {
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidUsernameException();
        }
        username = username.trim().toLowerCase();
        return bookByUserLibraryRepository.findAllByUsername(username);
    }
//endregion
}

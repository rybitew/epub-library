package pl.app.epublibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.app.epublibrary.exception.*;
import pl.app.epublibrary.model.book.BookByUserLibrary;
import pl.app.epublibrary.model.user.User;
import pl.app.epublibrary.model.user.UserRole;
import pl.app.epublibrary.repositories.book.BookByUserLibraryRepository;
import pl.app.epublibrary.repositories.book.UserLibraryByBookRepository;
import pl.app.epublibrary.repositories.user.UserRepository;

import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;
    private CommentService commentService;
    private RoleService roleService;
    private BookByUserLibraryRepository bookByUserLibraryRepository;
    private UserLibraryByBookRepository userLibraryByBookRepository;

    private static final String EMAIL_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    @Autowired
    public UserService(UserRepository userRepository, CommentService commentService,
                       RoleService roleService, BookByUserLibraryRepository bookByUserLibraryRepository,
                       UserLibraryByBookRepository userLibraryByBookRepository) {
        this.userRepository = userRepository;
        this.commentService = commentService;
        this.roleService = roleService;
        this.bookByUserLibraryRepository = bookByUserLibraryRepository;
        this.userLibraryByBookRepository = userLibraryByBookRepository;
    }

//region CRUD
    public void saveUser(User user)
            throws InvalidEmailException, InvalidUsernameException, InvalidEmailFormatException {
        if (user.getUsername() == null) {
            throw new InvalidUsernameException();
        }
        if (!user.getEmail().matches(EMAIL_PATTERN)) {
            throw new InvalidEmailFormatException();
        }
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

    public void deleteUser(String username) throws InvalidUsernameOrBookIdException {

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

    public void elevateUser(String username) {
        User user = userRepository.findByUsername(username);
        user.setElevated(true);
        userRepository.save(user);
    }
//endregion

//region ENDPOINT

    public boolean isUserElevated(String username) {
        return userRepository.findIfElevated(username);
    }
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void deleteFromUserLibrary(String username, UUID bookId) throws UnexpectedErrorException {
        try {
            bookByUserLibraryRepository.deleteByUsernameAndBookId(username, bookId);
            userLibraryByBookRepository.deleteByUsernameAndBookId(username, bookId);
        } catch (Exception e) {
            throw new UnexpectedErrorException();
        }
    }

    //TODO: add paging
    public Set<BookByUserLibrary> findAllUserLibraryBooks(String username) {
        return bookByUserLibraryRepository.findAllByUsername(username);
    }
//endregion

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

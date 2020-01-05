package pl.app.epublibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.app.epublibrary.exception.InvalidEmailException;
import pl.app.epublibrary.exception.InvalidUsernameException;
import pl.app.epublibrary.exception.InvalidUsernameOrBookIdException;
import pl.app.epublibrary.exception.UnexpectedErrorException;
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
    public void saveUser(User user) throws InvalidEmailException, InvalidUsernameException, UnexpectedErrorException {
        if (findUserByUsername(user.getUsername()) == null) {
            if (findUserByUsernameAndEmailAddress(user.getUsername(), user.getEmail()) == null) {
                userRepository.save(user);
                roleService.saveRole(new UserRole(user.getUsername(), "user"));
            } else {
                throw new InvalidEmailException();
            }
        } else {
            throw new InvalidUsernameException();
        }
    }

    public void deleteUser(String username) throws InvalidUsernameOrBookIdException {

        userRepository.deleteByUsername(username);
        roleService.deleteRole(username);
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
//endregion

//region ENDPOINT

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

    private User findUserByUsernameAndEmailAddress(String username, String email) {
        return userRepository.findByUsernameAndEmail(username, email);
    }
}

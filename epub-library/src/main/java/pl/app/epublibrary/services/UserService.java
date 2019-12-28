package pl.app.epublibrary.services;

import org.springframework.stereotype.Service;
import pl.app.epublibrary.repositories.user.CommentByUserNameRepository;
import pl.app.epublibrary.repositories.user.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;
    private CommentByUserNameRepository commentByUserNameRepository;
}

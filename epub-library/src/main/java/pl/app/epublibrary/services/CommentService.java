package pl.app.epublibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.app.epublibrary.model.comment.Comment;
import pl.app.epublibrary.model.comment.CommentByBook;
import pl.app.epublibrary.model.comment.CommentByUsername;
import pl.app.epublibrary.repositories.commnet.CommentByBookRepository;
import pl.app.epublibrary.repositories.commnet.CommentByUserNameRepository;

import java.time.Instant;
import java.util.UUID;

@Service
public class CommentService {

    private CommentByBookRepository commentByBookRepository;
    private CommentByUserNameRepository commentByUserNameRepository;

    @Autowired
    public CommentService(CommentByBookRepository commentByBookRepository, CommentByUserNameRepository commentByUserNameRepository) {
        this.commentByBookRepository = commentByBookRepository;
        this.commentByUserNameRepository = commentByUserNameRepository;
    }

    public Comment saveComment(Comment comment) {
        try {
            comment.setTimestamp(Instant.now());
            commentByBookRepository.save(new CommentByBook(comment));
            commentByUserNameRepository.save(new CommentByUsername(comment));
            return comment;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean deleteComment(Comment comment) {
        try {
            commentByUserNameRepository.deleteByBookIdAndUsername(comment.getBookId(), comment.getUsername());
            commentByBookRepository.deleteByBookIdAndUsername(comment.getBookId(), comment.getUsername());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteAllBookComments(UUID bookId) {
        try {
            commentByBookRepository.deleteAllByBookId(bookId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

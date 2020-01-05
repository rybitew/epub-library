package pl.app.epublibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.app.epublibrary.model.comment.Comment;
import pl.app.epublibrary.model.comment.CommentByBook;
import pl.app.epublibrary.model.comment.CommentByUsername;
import pl.app.epublibrary.model.comment.CommentId;
import pl.app.epublibrary.repositories.comment.CommentByBookRepository;
import pl.app.epublibrary.repositories.comment.CommentByUserNameRepository;
import pl.app.epublibrary.repositories.comment.CommentRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private CommentByBookRepository commentByBookRepository;
    private CommentByUserNameRepository commentByUserNameRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, CommentByBookRepository commentByBookRepository,
                          CommentByUserNameRepository commentByUserNameRepository) {
        this.commentRepository = commentRepository;
        this.commentByBookRepository = commentByBookRepository;
        this.commentByUserNameRepository = commentByUserNameRepository;
    }

//region CRUD
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
        commentByBookRepository.save(new CommentByBook(comment));
        commentByUserNameRepository.save(new CommentByUsername(comment));
    }

    public void deleteComment(Comment comment) {
        commentRepository.deleteById(comment.getId());
        commentByUserNameRepository.deleteByUsernameAndCommentId(comment.getUsername(), comment.getId());
        commentByBookRepository.deleteByBookIdAndCommentId(comment.getBookId(), comment.getId());
    }

    public boolean deleteAllBookComments(UUID bookId) {
        try {
            Set<UUID> commentsByBook = commentByBookRepository.findAllCommentsByBookId(bookId)
                    .stream()
                    .map(CommentId::getCommentId)
                    .collect(Collectors.toSet());
            List<Comment> comments = commentRepository.findAllById(commentsByBook);
            commentRepository.deleteAll(comments);
            commentByBookRepository.deleteAllByBookId(bookId);
            comments.forEach(comment ->
                    commentByUserNameRepository.deleteByUsernameAndCommentId(comment.getUsername(), comment.getId()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void deleteAllUserComments(String username) {
        Set<UUID> commentsByUsername = commentByUserNameRepository.findAllCommentsByUsername(username)
                .stream()
                .map(CommentId::getCommentId)
                .collect(Collectors.toSet());
        List<Comment> comments = commentRepository.findAllById(commentsByUsername);
        commentRepository.deleteAll(comments);
        commentByUserNameRepository.deleteAllByUsername(username);
        comments.forEach(comment ->
                commentByBookRepository.deleteByBookIdAndCommentId(comment.getBookId(), comment.getId()));
    }
//endregion

    public Set<Comment> findAllCommentsByUser(String username) {
        Set<UUID> commentIds = commentByUserNameRepository.findAllCommentsByUsername(username)
                .stream()
                .map(CommentId::getCommentId)
                .collect(Collectors.toSet());

        Set<Comment> comments = new HashSet<>();
        commentIds.forEach(id -> commentRepository.findById(id).ifPresent(comments::add));
        return comments;
    }

    public Set<Comment> findAllCommentsByBook(UUID bookId) {
        Set<UUID> commentIds = commentByBookRepository.findAllCommentsByBookId(bookId)
                .stream()
                .map(CommentId::getCommentId)
                .collect(Collectors.toSet());

        Set<Comment> comments = new HashSet<>();
        commentIds.forEach(id -> commentRepository.findById(id).ifPresent(comments::add));
        return comments;
    }
}

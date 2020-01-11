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

import java.util.*;
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
        if (!comment.getComment().isEmpty()) {
            commentRepository.save(comment);
            commentByBookRepository.save(new CommentByBook(comment));
            commentByUserNameRepository.save(new CommentByUsername(comment));
        }
    }

    public void deleteComment(Comment comment) {
        commentRepository.deleteCommentById(comment.getId());
        commentByUserNameRepository.deleteByUsernameAndCommentIdAndTimestamp(
                comment.getUsername(), comment.getId(), comment.getTimestamp()
        );
        commentByBookRepository.deleteByBookIdAndCommentIdAndTimestamp(
                comment.getBookId(), comment.getId(), comment.getTimestamp()
        );
    }

    public boolean deleteAllBookComments(UUID bookId) {
        try {
            Set<UUID> commentsByBook = commentByBookRepository.findAllCommentsByBookId(bookId)
                    .stream()
                    .map(CommentId::getCommentId)
                    .collect(Collectors.toSet());
            List<Comment> comments = commentRepository.findAllCommentsById(commentsByBook);
            commentRepository.deleteAll(comments);
            commentByBookRepository.deleteAllByBookId(bookId);
            comments.forEach(comment ->
                    commentByUserNameRepository.deleteByUsernameAndCommentIdAndTimestamp(
                            comment.getUsername(), comment.getId(), comment.getTimestamp())
            );
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
        List<Comment> comments = commentRepository.findAllCommentsById(commentsByUsername);
        commentRepository.deleteAll(comments);
        commentByUserNameRepository.deleteAllByUsername(username);
        comments.forEach(comment ->
                commentByBookRepository.deleteByBookIdAndCommentIdAndTimestamp(
                        comment.getBookId(), comment.getId(), comment.getTimestamp())
        );
    }
//endregion

    public List<Comment> findAllCommentsByUser(String username) {
        List<UUID> commentIds = commentByUserNameRepository.findAllCommentsByUsername(username)
                .stream()
                .map(CommentId::getCommentId)
                .collect(Collectors.toList());

        List<Comment> comments = new LinkedList<>();
        commentIds.forEach(id -> commentRepository.findCommentById(id).ifPresent(comments::add));
//        comments.sort((Comparator.comparing(Comment::getTimestamp)));
        return comments;
    }

    public List<Comment> findAllCommentsByBook(UUID bookId) {
        List<UUID> commentIds = commentByBookRepository.findAllCommentsByBookId(bookId)
                .stream()
                .map(CommentId::getCommentId)
                .collect(Collectors.toList());

        List<Comment> comments = new LinkedList<>();
        commentIds.forEach(id -> commentRepository.findCommentById(id).ifPresent(comments::add));
//        comments.sort((Comparator.comparing(Comment::getTimestamp)));
        return comments;
    }
}

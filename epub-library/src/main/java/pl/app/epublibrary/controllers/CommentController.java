package pl.app.epublibrary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.app.epublibrary.dto.CommentDto;
import pl.app.epublibrary.model.comment.Comment;
import pl.app.epublibrary.services.CommentService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping(value = "/book/comment/add/")
    public void addComment(@RequestBody Comment comment) {
        try {
            comment.setTimestamp(Instant.now());
            comment.setId(UUID.randomUUID());
            commentService.saveComment(comment);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error", e);
        }
    }

    @DeleteMapping(value = "/book/comment/delete/")
    public void deleteComment(@RequestBody CommentDto comment) {
        try {
            commentService.deleteComment(new Comment(comment));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error", e);
        }
    }

    @GetMapping(value = "/user/comment/get/", params = {"username"})
    public List<CommentDto> getUserComments(@RequestParam(value = "username") String username) {
        try {
            return commentService.findAllCommentsByUser(username)
                    .stream()
                    .map(CommentDto::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error", e);
        }
    }

    @GetMapping(value = "/book/comment/get/", params = {"id"})
    public List<CommentDto> getBookComments(@RequestParam(value = "id") String bookId) {
        try {
            return commentService.findAllCommentsByBook(UUID.fromString(bookId))
                    .stream()
                    .map(CommentDto::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error", e);
        }
    }
}

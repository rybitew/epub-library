package pl.app.epublibrary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.app.epublibrary.model.book.BookByAuthor;
import pl.app.epublibrary.model.book.BookByAuthorPublisher;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookByAuthorDto {

    private String[] authors;
    private UUID bookId;
    private String title;
    private String publisher;

    public BookByAuthorDto(BookByAuthor book) {
        this.authors = new String[]{book.getAuthors()};
        this.bookId = book.getBookId();
        this.title = book.getTitle();
        this.publisher = book.getPublisher();
    }

    public BookByAuthorDto(BookByAuthorPublisher book) {
        this.authors = new String[]{book.getAuthors()};
        this.bookId = book.getBookId();
        this.title = book.getTitle();
        this.publisher = book.getPublisher();
    }
}

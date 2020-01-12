package pl.app.epublibrary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.app.epublibrary.model.book.BookByAuthor;

import java.lang.reflect.Array;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookByAuthorDto {

    private String[] authors;
    private UUID bookId;
    private String title;

    public BookByAuthorDto(BookByAuthor book) {
        this.authors = new String[]{book.getAuthors()};
        this.bookId = book.getBookId();
        this.title = book.getTitle();
    }
}

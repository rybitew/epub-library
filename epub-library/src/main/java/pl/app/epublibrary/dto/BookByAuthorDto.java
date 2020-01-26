package pl.app.epublibrary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.app.epublibrary.model.book.BookByAuthor;
import pl.app.epublibrary.model.book.BookByAuthorPublisher;

import java.util.Arrays;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookByAuthorDto that = (BookByAuthorDto) o;
        return Arrays.equals(Arrays.stream(authors).map(author -> author.trim().toLowerCase()).toArray(),
                Arrays.stream(that.authors).map(author -> author.trim().toLowerCase()).toArray()) &&
                Objects.equals(bookId, that.bookId) &&
                Objects.equals(title.trim().toLowerCase(), that.title.trim().toLowerCase()) &&
                Objects.equals(publisher.trim().toLowerCase(), that.publisher.trim().toLowerCase());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(bookId, title, publisher);
        result = 31 * result + Arrays.hashCode(authors);
        return result;
    }
}

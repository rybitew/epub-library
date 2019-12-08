package pl.app.epublibrary.services.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.app.epublibrary.model.author.Author;
import pl.app.epublibrary.model.book.*;
import pl.app.epublibrary.repositories.book.BookRepository;
import pl.app.epublibrary.repositories.book.*;
import pl.app.epublibrary.services.AuthorService;

import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {

    private BookRepository bookRepository;
    private AuthorService authorService;
    private PublisherService publisherService;

    private BookByAuthorRepository bookByAuthorRepository;
    private BookByPublisherRepository bookByPublisherRepository;
    private BookByReleaseDateRepository bookByReleaseDateRepository;
    private BookByTitleRepository bookByTitleRepository;

    @Autowired
    public BookService(BookRepository bookRepository, BookByAuthorRepository bookByAuthorRepository,
                       PublisherService publisherService, AuthorService authorService,
                       BookByPublisherRepository bookByPublisherRepository,
                       BookByReleaseDateRepository bookByReleaseDateRepository,
                       BookByTitleRepository bookByTitleRepository) {
        this.bookRepository = bookRepository;
        this.bookByAuthorRepository = bookByAuthorRepository;
        this.publisherService = publisherService;
        this.authorService = authorService;
        this.bookByPublisherRepository = bookByPublisherRepository;
        this.bookByReleaseDateRepository = bookByReleaseDateRepository;
        this.bookByTitleRepository = bookByTitleRepository;
    }

    public void saveBook(Book book) {
        Book existingBook = this.getExistingBook(book);
        if (existingBook == null || !existingBook.equals(book)) {
            book.getAuthors().forEach((uuid, name) -> authorService.saveAuthor(new Author(uuid, name)));
            bookRepository.save(book);
            this.saveAllBookTables(book);
        }
    }

    public void updateBook(Book book) {

    }

    public void deleteBook(UUID id) {
        bookRepository.deleteById(id);
        bookByTitleRepository.deleteByBookId(id);
        bookByReleaseDateRepository.deleteByBookId(id);
        bookByPublisherRepository.deleteByBookId(id);
        bookByAuthorRepository.deleteByBookId(id);
    }

    private Book getExistingBook(Book book) {
        return book
            .getAuthors()
            .values().stream()
            .filter(a -> authorService.findByName(a) != null)
            .map(a -> Optional.ofNullable(bookByAuthorRepository.findByAuthorAndTitle(a, book.getTitle())))
            .map(a -> a.map(bookByAuthor -> bookRepository.getById(bookByAuthor.getBookId())).get())
            .findFirst().get();


//        for (String author : book.getAuthors().values()) {
//            if (authorService.findByName(author) != null) {
//                BookByAuthor bookByAuthor = bookByAuthorRepository.findByAuthorAndTitle(author, book.getTitle());
//                if (bookByAuthor != null)
//                    return bookRepository.getById(bookByAuthor.getBookId());
//            }
//        }
//        return null;
    }

    private void saveAllBookTables(Book book) {
        book.getAuthors().forEach(
                (uuid, name) -> bookByAuthorRepository.save(new BookByAuthor(name, book.getId(), uuid, book.getTitle()))
        );
        if (book.getPublisher() != null)
            bookByPublisherRepository.save(new BookByPublisher(book.getPublisher(), book.getId(), book.getTitle()));
        if (book.getReleaseDate() != null)
            bookByReleaseDateRepository.save(new BookByReleaseDate(book.getReleaseDate(), book.getId(), book.getTitle()));
        bookByTitleRepository.save(new BookByTitle(book.getTitle(), book.getId()));
    }
}

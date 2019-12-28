package pl.app.epublibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.app.epublibrary.model.author.Author;
import pl.app.epublibrary.model.book.*;
import pl.app.epublibrary.repositories.book.BookRepository;
import pl.app.epublibrary.repositories.book.*;
import pl.app.epublibrary.services.AuthorService;

import java.util.*;

@Service
public class BookService {

    private BookRepository bookRepository;
    private AuthorService authorService;

    private BookByAuthorRepository bookByAuthorRepository;
    private BookByPublisherRepository bookByPublisherRepository;
    private BookByReleaseDateRepository bookByReleaseDateRepository;
    private BookByTitleRepository bookByTitleRepository;

    @Autowired
    public BookService(BookRepository bookRepository,
                       BookByAuthorRepository bookByAuthorRepository, AuthorService authorService,
                       BookByPublisherRepository bookByPublisherRepository,
                       BookByReleaseDateRepository bookByReleaseDateRepository,
                       BookByTitleRepository bookByTitleRepository) {
        this.bookRepository = bookRepository;
        this.bookByAuthorRepository = bookByAuthorRepository;
        this.authorService = authorService;
        this.bookByPublisherRepository = bookByPublisherRepository;
        this.bookByReleaseDateRepository = bookByReleaseDateRepository;
        this.bookByTitleRepository = bookByTitleRepository;
    }

// region CRUD

    public void saveBook(Book book) {
//        Map<UUID, String> savedAuthors = new HashMap<>();
//        book.getAuthors().forEach((uuid, name) -> {
//            Author author = authorService.saveAuthor(new Author(uuid, name));
//            authorService.updateAuthor(author, book.getTitle());
//            savedAuthors.put(author.getId(), author.getName());
//        });
        //chyba niepotrzebne
        //book.setAuthors(saveBookAuthors(book.getAuthors(), book.getTitle()));

        Book existingBook = this.getExistingBook(book);
        if (existingBook == null || !existingBook.equals(book)) {
            bookRepository.save(book);
            this.saveAllBookTables(book);
        }
    }

    public void updateBook(Book book) {
//        BookByAuthor bookToUpdate = bookByAuthorRepository
//                .findByAuthorAndTitle(book.getAuthors().entrySet().iterator().next().getValue(), book.getTitle());
//        book.setId(bookToUpdate.getBookId());
//        book.getAuthors().forEach((id, name) -> {
//            BookByAuthor bookByAuthor = bookByAuthorRepository.findByAuthorAndTitle(name, book.getTitle());
//            bookByAuthor.set
//            bookByAuthorRepository.save()
//        });
//        bookByAuthorRepository.save(new BookByAuthor(
//                bookToUpdate.getAuthor(),
//                book.getId(),
//                bookToUpdate.getAuthorId(),
//                bookToUpdate.getTitle()));
//
//        bookRepository.save()
    }

    /**
     * Deletes the book from all of the tables. Also deletes the authors of the deleted book if they don't have
     * any books
     * @param id ID of the book to delete
     */
    public void deleteBook(UUID id) {
        Book book = bookRepository.findById(id).orElse(null);

        if (book != null) {
            bookRepository.deleteById(id);
            bookByTitleRepository.deleteByBookIdAndTitle(id, book.getTitle());
            bookByReleaseDateRepository.deleteByBookIdAndReleaseDate(id, book.getReleaseDate());
//            bookByPublisherRepository.deleteByBookIdAndPublisherName(id, book.getPublisher());

            //Delete every author of the deleted book who has no books
            book.getAuthors().forEach((a) -> bookByAuthorRepository.deleteByBookIdAndAuthor(id, a));
        }
//            for (String author : book.getAuthors().values()) {
//                bookByAuthorRepository.deleteByBookIdAndAuthor(id, author);
//                if (bookByAuthorRepository.findById(author).isEmpty())
//                    authorService.deleteAuthor(book.getAuthors().);
//            }
    }
//endregion

//region GETTERS

    public List<BookByAuthor> findBooksByAuthor(String author) {
        return bookByAuthorRepository.findAllByAuthor(author);
    }

    public Book findBookById(UUID id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<BookByTitle> findBooksByTitle(String title) {
        return bookByTitleRepository.findAllByTitle(title);
    }

    public List<BookByPublisher> findBooksByPublisher(String publisher) {
        return bookByPublisherRepository.findAllByPublisherName(publisher);
    }
//endregion

//region UTIL
    private Book getExistingBook(Book book) {
        //TODO: make sure it works (for now it does)
        return book
                .getAuthors()
                .stream()
//                .filter(a -> authorService.findByName(a) != null)
                .map(a -> Optional.ofNullable(bookByAuthorRepository.findByAuthorAndTitle(a, book.getTitle())))
                .filter(Optional::isPresent)
                .map(a -> a.map(bookByAuthor -> bookRepository.getById(bookByAuthor.getBookId())))
                .findFirst()
                .orElse(Optional.empty())
                .orElse(null);


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
                (a) -> bookByAuthorRepository.save(new BookByAuthor(a, book.getId(), book.getTitle()))
        );
        if (book.getPublisher() != null)
            bookByPublisherRepository.save(new BookByPublisher(book.getPublisher(), book.getId()));
        if (book.getReleaseDate() != null)
            bookByReleaseDateRepository.save(new BookByReleaseDate(book.getReleaseDate(), book.getId()));
        bookByTitleRepository.save(new BookByTitle(book.getTitle(), book.getId()));
    }

/*    private Map<UUID, String> saveBookAuthors(Map<UUID, String> authors, String title) {
        Map<UUID, String> savedAuthors = new HashMap<>();
        authors.forEach((uuid, name) -> {
            Author author = authorService.saveAuthor(new Author(uuid, name));
            authorService.updateAuthor(author.getName(), author.getId(), title);
            savedAuthors.put(author.getId(), author.getName());
        });

        return savedAuthors;
    }*/
//endregion
}

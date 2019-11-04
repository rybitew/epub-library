package pl.app.epublibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.app.epublibrary.entities.Book;
import pl.app.epublibrary.repositories.BookRepository;

import java.util.List;

@Service
public class BookService implements EntityService<Book>{

    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @Override
    public void saveEntity(Book entity) {
        bookRepository.save(entity);
    }

    @Override
    public void updateEntity(Book entity) {

    }

    public List<Book> findByTitle(String title) {
        return null;
    }

    public List<Book> findByAuthor(String name, String surname) {
        return null;
    }
    public void deleteEntity(String name) {

    }
}

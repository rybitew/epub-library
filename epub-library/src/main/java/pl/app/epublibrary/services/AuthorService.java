package pl.app.epublibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.app.epublibrary.model.author.Author;
import pl.app.epublibrary.model.author.AuthorByName;
import pl.app.epublibrary.repositories.author.AuthorByNameRepository;
import pl.app.epublibrary.repositories.author.AuthorRepository;
import pl.app.epublibrary.repositories.book.BookByAuthorRepository;

import java.util.LinkedList;

@Service
public class AuthorService {

    private AuthorRepository authorRepository;
    private AuthorByNameRepository authorByNameRepository;
    private BookByAuthorRepository bookByAuthorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository, AuthorByNameRepository authorByNameRepository, BookByAuthorRepository bookByAuthorRepository) {
        this.authorRepository = authorRepository;
        this.authorByNameRepository = authorByNameRepository;
        this.bookByAuthorRepository = bookByAuthorRepository;
    }

    public Author saveAuthor(Author author) {
        Author existingEntity = this.getExistingEntity(author);
        if (!author.equals(existingEntity)) {
            this.saveAllAuthorTables(author);
            return authorRepository.save(author);
        }
        return existingEntity;
    }

    public void updateAuthor(Author author) {

    }

    public void deleteAuthor(Author author) {

    }

    public AuthorByName findByName(String name) {
        return authorByNameRepository.findByName(name);
    }

    public AuthorByName findByNameAndTitle(String name, String title) {
        return authorByNameRepository.findByNameAndTitle(name, title);
    }

    private Author getExistingEntity(Author author) {
        AuthorByName authorByName = this.findByName(author.getName());
        return authorByName == null ? null : authorRepository.getById(authorByName.getAuthorId());
    }

    private void saveAllAuthorTables(Author author) {
        authorByNameRepository.save(new AuthorByName(author.getName(), new LinkedList<>(), author.getId()));
    }
}

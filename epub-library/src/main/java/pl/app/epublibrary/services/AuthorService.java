package pl.app.epublibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.app.epublibrary.model.author.Author;
import pl.app.epublibrary.model.author.AuthorByName;
import pl.app.epublibrary.repositories.author.AuthorByNameRepository;
import pl.app.epublibrary.repositories.author.AuthorRepository;
import pl.app.epublibrary.repositories.book.BookByAuthorRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

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

//region CRUD
    public Author saveAuthor(Author author) {
        List<Author> existingEntity = this.getExistingEntity(author);

        if (existingEntity != null) {
            for (Author author1 : existingEntity) {
                if (!author.equals(author1)) {
                    saveAllAuthorTables(author);
                    return authorRepository.save(author);
                } else {
                    return author1;
                }
            }
        }

        saveAllAuthorTables(author);
        return authorRepository.save(author);
    }

    /**
     * Adds new book to the list of books in the AuthorByName table
     * @param title book to add
     */
    public void updateAuthor(String name, UUID id, String title) {
        AuthorByName authorToUpdate = authorByNameRepository.findByNameAndAuthorId(name, id);

        List<String> titles = authorToUpdate.getTitles();
        if (titles == null)
            titles = new LinkedList<>();
        if (!titles.contains(title)) {
            titles.add(title);
            authorToUpdate.setTitles(titles);
            authorByNameRepository.save(authorToUpdate);
        }
    }

    public void deleteAuthor(Author author) {
        authorByNameRepository.deleteByAuthorIdAndAndName(author.getId(), author.getName());
        authorRepository.delete(author);
    }
//endregion

//region GETTERS
    public List<AuthorByName> findByName(String name) {
        return authorByNameRepository.findAllByName(name);
    }
//endregion

/*
// Is it needed?
    public AuthorByName findByNameAndTitle(String name, String title) {
        return authorByNameRepository.findByNameAndTitles(name, title);
    }
*/

    private List<Author> getExistingEntity(Author author) {
        List<AuthorByName> authorsByName = findByName(author.getName());
        List<Author> authors = new LinkedList<>();

        if (authorsByName == null) {
            return null;
        }

        authorsByName.forEach(a -> authorRepository.getById(a.getAuthorId()).ifPresent(authors::add));
        return authors;
    }

    private void saveAllAuthorTables(Author author) {
        authorByNameRepository.save(new AuthorByName(author.getName(), null, author.getId()));
    }
}

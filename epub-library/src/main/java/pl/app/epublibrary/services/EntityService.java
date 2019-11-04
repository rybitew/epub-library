package pl.app.epublibrary.services;

public interface EntityService<T> {

    void saveEntity(T entity);
    void updateEntity(T entity);
}

package pl.app.epublibrary.exception;

public class BookAlreadyExistsException extends Exception {

    public BookAlreadyExistsException() {
        super();
    }

    public BookAlreadyExistsException(String message) {
        super(message);
    }
}

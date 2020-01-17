package pl.app.epublibrary.exception;

public class InsufficientBookDataException extends Exception {
    public InsufficientBookDataException() {
        super();
    }

    public InsufficientBookDataException(String message) {
        super(message);
    }

    public InsufficientBookDataException(String message, Throwable cause) {
        super(message, cause);
    }
}

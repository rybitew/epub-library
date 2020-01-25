package pl.app.epublibrary.exceptions;

public class InvalidEntityException extends Exception {
    public InvalidEntityException() {
        super();
    }

    public InvalidEntityException(String message) {
        super(message);
    }

    public InvalidEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}

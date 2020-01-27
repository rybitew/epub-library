package pl.app.epublibrary.exceptions;

public class CannotDeleteFileException extends Exception {
    public CannotDeleteFileException() {
        super();
    }

    public CannotDeleteFileException(String message) {
        super(message);
    }

    public CannotDeleteFileException(String message, Throwable cause) {
        super(message, cause);
    }
}

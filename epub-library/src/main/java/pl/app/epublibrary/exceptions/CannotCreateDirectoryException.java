package pl.app.epublibrary.exceptions;

public class CannotCreateDirectoryException extends Exception {
    public CannotCreateDirectoryException() {
        super();
    }

    public CannotCreateDirectoryException(String message) {
        super(message);
    }
}

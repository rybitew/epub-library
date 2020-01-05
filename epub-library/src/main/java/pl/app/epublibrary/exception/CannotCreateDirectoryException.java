package pl.app.epublibrary.exception;

public class CannotCreateDirectoryException extends Exception {
    public CannotCreateDirectoryException() {
        super();
    }

    public CannotCreateDirectoryException(String message) {
        super(message);
    }
}

package pl.app.epublibrary.exception;

public class InvalidFileNameException extends Exception {

    public InvalidFileNameException() {
        super();
    }

    public InvalidFileNameException(String message) {
        super(message);
    }

    public InvalidFileNameException(String message, Throwable cause) {
        super(message, cause);
    }
}

package pl.app.epublibrary.exceptions;

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

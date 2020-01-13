package pl.app.epublibrary.exception;

public class InvalidEmailFormatException extends Exception {
    public InvalidEmailFormatException() {
        super();
    }

    public InvalidEmailFormatException(String message) {
        super(message);
    }

    public InvalidEmailFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}

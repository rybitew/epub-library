package pl.app.epublibrary.exception;

public class InvalidBookIdException extends Exception {

    public InvalidBookIdException() {
        super();
    }

    public InvalidBookIdException(String message) {
        super(message);
    }
}

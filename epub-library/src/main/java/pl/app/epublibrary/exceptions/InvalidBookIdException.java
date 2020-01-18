package pl.app.epublibrary.exceptions;

public class InvalidBookIdException extends Exception {

    public InvalidBookIdException() {
        super();
    }

    public InvalidBookIdException(String message) {
        super(message);
    }
}

package pl.app.epublibrary.exceptions;

public class InvalidUsernameOrBookIdException extends Exception {

    public InvalidUsernameOrBookIdException() {
        super();
    }

    public InvalidUsernameOrBookIdException(String message) {
        super(message);
    }
}

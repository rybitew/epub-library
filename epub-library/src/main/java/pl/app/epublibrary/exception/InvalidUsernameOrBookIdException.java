package pl.app.epublibrary.exception;

public class InvalidUsernameOrBookIdException extends Exception {

    public InvalidUsernameOrBookIdException() {
        super();
    }

    public InvalidUsernameOrBookIdException(String message) {
        super(message);
    }
}

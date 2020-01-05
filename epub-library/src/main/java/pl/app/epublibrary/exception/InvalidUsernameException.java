package pl.app.epublibrary.exception;

public class InvalidUsernameException extends Exception {
    public InvalidUsernameException() {
        super();
    }

    public InvalidUsernameException(String message) {
        super(message);
    }
}

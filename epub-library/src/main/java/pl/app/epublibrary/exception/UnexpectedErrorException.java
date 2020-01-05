package pl.app.epublibrary.exception;

public class UnexpectedErrorException extends Exception {

    public UnexpectedErrorException(String message) {
        super(message);
    }

    public UnexpectedErrorException() {
        super();
    }
}

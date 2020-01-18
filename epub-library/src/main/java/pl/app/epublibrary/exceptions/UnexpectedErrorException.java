package pl.app.epublibrary.exceptions;

public class UnexpectedErrorException extends Exception {

    public UnexpectedErrorException(String message) {
        super(message);
    }

    public UnexpectedErrorException() {
        super();
    }
}

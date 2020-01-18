package pl.app.epublibrary.exceptions;

public class InsufficientParametersException extends Exception {

    public InsufficientParametersException() {
        super();
    }

    public InsufficientParametersException(String message) {
        super(message);
    }

    public InsufficientParametersException(String message, Throwable cause) {
        super(message, cause);
    }
}

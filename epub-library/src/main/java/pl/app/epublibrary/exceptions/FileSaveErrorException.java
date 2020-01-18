package pl.app.epublibrary.exceptions;

public class FileSaveErrorException extends Exception {

    public FileSaveErrorException() {
        super();
    }

    public FileSaveErrorException(String message) {
        super(message);
    }

    public FileSaveErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}

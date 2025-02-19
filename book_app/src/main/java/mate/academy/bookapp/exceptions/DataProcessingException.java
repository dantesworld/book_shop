package mate.academy.bookapp.exceptions;

public class DataProcessingException extends RuntimeException {
    public DataProcessingException(String message, Throwable ex) {
        super(message, ex);
    }

    public DataProcessingException(String message) {
        super(message);
    }
}

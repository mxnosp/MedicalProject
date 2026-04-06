package model.exceptions;

/**
 * InvalidNameException will be thrown when a name is not valid
 */
public class InvalidNameException extends ValidationException {

    public InvalidNameException(String message) {
        super(message);
    }

    public InvalidNameException(String message, Throwable cause) {
        super(message, cause);
    }
}

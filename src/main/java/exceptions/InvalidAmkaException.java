package exceptions;

/**
 * InvalidAmkaException is thrown when an invalid amka is provided by the user
 */
public class InvalidAmkaException extends ValidationException {

    public InvalidAmkaException(String message) {
        super(message);
    }

    public InvalidAmkaException(String message,Throwable cause) {
        super(message,cause);
    }

}

package model.exceptions;


/**
 * InvalidDateException is the exception to be thrown when a date that is provided by the user is not a valid date
 */
public class InvalidDateException extends ValidationException{

    public InvalidDateException(String message) {
        super(message);
    }

    public InvalidDateException(String message, Throwable cause) {
        super(message, cause);
    }

}

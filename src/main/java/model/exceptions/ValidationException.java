package model.exceptions;

/**
 * ValidationExcpeption class represents the general class for the excpetions that will be thrown if the data
 * provided e.g. to make an object of a patient are not valid
 */
public class ValidationException extends RuntimeException{

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

package model.exceptions;

/**
 * exception to be thrown when the user gives invalid spirometry values
 */
public class InvalidSpirometryValueInput extends RuntimeException {
    public InvalidSpirometryValueInput(String message) {
        super(message);
    }
    public InvalidSpirometryValueInput(String message,Throwable cause) {
        super(message,cause);
    }

}

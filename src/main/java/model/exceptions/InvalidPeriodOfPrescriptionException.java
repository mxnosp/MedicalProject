package model.exceptions;

/**
 * InvalidPeriodOfPrescriptionException when the period the prescription will last is invalid
 */
public class InvalidPeriodOfPrescriptionException extends ValidationException {
    public InvalidPeriodOfPrescriptionException(String message) {
        super(message);
    }

    public InvalidPeriodOfPrescriptionException(String message,Throwable cause) {
        super(message,cause);
    }
}

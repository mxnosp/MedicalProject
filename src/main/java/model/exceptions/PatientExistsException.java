package model.exceptions;

/**
 * Exception to be thrown when we try to insert a patient with an amka that is already registered
 */
public class PatientExistsException extends RuntimeException{

    public PatientExistsException(String msg){
        super(msg);
    }

    public PatientExistsException(String msg,Throwable cause){
        super(msg,cause);
    }
}

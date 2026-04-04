package exceptions;

/**
 * InvalidPhoneException is thrown when an invalid phone is provided by the user
 */
public class InvalidPhoneException extends ValidationException{

    public InvalidPhoneException(String message){
        super(message);
    }

    public InvalidPhoneException(String message,Throwable cause){
        super(message,cause );
    }
}


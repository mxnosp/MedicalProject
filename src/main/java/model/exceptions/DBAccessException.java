package model.exceptions;


/**
 * DBAccessException will be thrown whenever insert delete or search fails
 */
public class DBAccessException extends RuntimeException {

    public DBAccessException(String message) {
        super(message);
    }

    public DBAccessException(String message,Throwable cause) {
        super(message,cause);
    }


}

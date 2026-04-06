package model.exceptions;

/**
 * DBCreationException will be thrown when th DBConnector failed to create the db in the given directory
 */
public class DBCreationException extends RuntimeException{

    public DBCreationException(String msg){
        super(msg);
    }

    public DBCreationException(String msg ,Throwable cause){
        super(msg,cause);
    }
}

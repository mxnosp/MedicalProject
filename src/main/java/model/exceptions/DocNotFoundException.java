package model.exceptions;

/**
 * DocNotFoundException is the exception to be thrown when the user didn't provide a correct path for a document
 */
public class DocNotFoundException extends ValidationException{

        public DocNotFoundException(String msg){
            super(msg);
        }

        public DocNotFoundException(String msg,Throwable cause){
            super(msg,cause);
        }
}

package com.APP.Project.UserCoreLogic.exceptions;

/**
 * Exception thrown when an invalid map file is encountered.
 * It extends the UserCoreLogicException class.
 * 
 * @author Rikin Dipakkumar Chauhan
 */
public class InvalidMapException extends UserCoreLogicException{

     /**
     * Constructs an InvalidMapException with the specified detail message.
     *
     * @param p_exceptionMessage detailed Exception message
     */
     public InvalidMapException(String p_exceptionMessage) {
          super(p_exceptionMessage);
     }

     /**
     * Constructs an InvalidMapException with the specified detail message and cause.
     *
     * @param p_exceptionMessage detailed Exception message
     * @param p_exceptionCause detailed cause that causes the current exception
     */
     public InvalidMapException(String p_exceptionMessage, Throwable p_exceptionCause) {
          super(p_exceptionMessage, p_exceptionCause);
     }
}

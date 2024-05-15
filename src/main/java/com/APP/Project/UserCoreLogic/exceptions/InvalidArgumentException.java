package com.APP.Project.UserCoreLogic.exceptions;

/** 
 * This exception is thrown when an invalid argument is passed to a method.
 * It extends the UserCoreLogicException class.
 * 
 * @author Rikin Dipakkumar Chauhan
 */
public class InvalidArgumentException extends UserCoreLogicException {

    /**
     * Constructs an InvalidArgumentException with the specified detail message.
     *
     * @param p_exceptionMessage detailed Exception message
     */
    public InvalidArgumentException(String p_exceptionMessage) {
        super(p_exceptionMessage);
    }

    /**
     * Constructs an InvalidArgumentException with the specified detail message and cause.
     *
     * @param p_exceptionMessage detailed Exception message
     * @param p_exceptionCause detailed cause that causes the current exception
     */
    public InvalidArgumentException(String p_exceptionMessage, Throwable p_exceptionCause) {
        super(p_exceptionMessage, p_exceptionCause);
    }
}

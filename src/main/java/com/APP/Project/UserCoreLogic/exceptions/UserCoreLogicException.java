package com.APP.Project.UserCoreLogic.exceptions;

/**
 * Custom exception class for handling user core logic exceptions.
 * It extends the base Exception class.
 *
 * @author Rikin Dipakkumar Chauhan
 */
public class UserCoreLogicException extends Exception {

     /**
     * Constructs an UserCoreLogicException with the specified detail message.
     *
     * @param p_exceptionMessage detailed Exception message
     */
     public UserCoreLogicException(String p_exceptionMessage) {
          super(p_exceptionMessage);
     }

     /**
     * Constructs an UserCoreLogicException with the specified detail message and cause.
     *
     * @param p_exceptionMessage detailed Exception message
     * @param p_exceptionCause detailed cause that causes the current exception
     */
     public UserCoreLogicException(String p_exceptionMessage, Throwable p_exceptionCause) {
          super(p_exceptionMessage, p_exceptionCause);
     }
}

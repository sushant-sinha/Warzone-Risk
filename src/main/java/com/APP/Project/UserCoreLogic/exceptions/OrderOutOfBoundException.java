package com.APP.Project.UserCoreLogic.exceptions;

/**
 * The OrderOutOfBoundException class represents an exception that is thrown
 * when player doesn't have any remaining order for execution.
 * It extends the UserCoreLogicException class.
 * 
 * @author Rikin Dipakkumar Chauhan
 */
public class OrderOutOfBoundException extends UserCoreLogicException {

     /**
     * Constructs an OrderOutOfBoundException with the specified detail message.
     *
     * @param p_exceptionMessage detailed Exception message
     */
     public OrderOutOfBoundException(String p_exceptionMessage) {
          super(p_exceptionMessage);
     }

     /**
     * Constructs an OrderOutOfBoundException with the specified detail message and cause.
     *
     * @param p_exceptionMessage detailed Exception message
     * @param p_exceptionCause detailed cause that causes the current exception
     */
     public OrderOutOfBoundException(String p_exceptionMessage, Throwable p_exceptionCause) {
          super(p_exceptionMessage, p_exceptionCause);
     }
}

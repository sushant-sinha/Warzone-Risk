package com.APP.Project.UserCoreLogic.exceptions;

/**
 * Represents an exception thrown when a player doesn't have enough number of reinforcements required to issue the order.
 * It extends the UserCoreLogicException class.
 *
 * @author Rikin Dipakkumar Chauhan
 */
public class ReinforcementOutOfBoundException extends UserCoreLogicException{

     /**
     * Constructs an ReinforcementOutOfBoundException with the specified detail message.
     *
     * @param p_exceptionMessage detailed Exception message
     */
     public ReinforcementOutOfBoundException(String p_exceptionMessage) {
          super(p_exceptionMessage);
     }

     /**
     * Constructs an ReinforcementOutOfBoundException with the specified detail message and cause.
     *
     * @param p_exceptionMessage detailed Exception message
     * @param p_exceptionCause detailed cause that causes the current exception
     */
     public ReinforcementOutOfBoundException(String p_exceptionMessage, Throwable p_exceptionCause) {
          super(p_exceptionMessage, p_exceptionCause);
     }
}

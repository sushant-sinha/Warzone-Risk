package com.APP.Project.UserCoreLogic.exceptions;

/**
 * This exception represents an illegal state in the game loop.
 * It extends the UserCoreLogicException class.
 *
 * @author Rikin Dipakkumar Chauhan
 */
public class GameLoopIllegalStateException extends UserCoreLogicException{

     /**
     * Constructs an GameLoopIllegalStateException with the specified detail message.
     *
     * @param p_exceptionMessage detailed Exception message
     */
     public GameLoopIllegalStateException(String p_exceptionMessage) {
          super(p_exceptionMessage);
     }

     /**
     * Constructs a new GameLoopIllegalStateException with the specified detail message and cause.
     *
     * @param p_exceptionMessage detailed Exception message
     * @param p_exceptionCause detailed cause that causes the current exception
     */
     public GameLoopIllegalStateException(String p_exceptionMessage, Throwable p_exceptionCause) {
          super(p_exceptionMessage, p_exceptionCause);
     }
}

package com.APP.Project.UserCoreLogic.exceptions;

/**
 * This exception indicates that a required tag is absent in .map file.
 * It extends the UserCoreLogicException class.
 *
 * @author Rikin Dipakkumar Chauhan
 */
public class AbsentTagException extends UserCoreLogicException {

     /**
     * Constructs an AbsentTagException with the specified detail message.
     *
     * @param p_exceptionMessage detailed Exception message
     */
     public AbsentTagException(String p_exceptionMessage) {
          super(p_exceptionMessage);
     }

     /**
     * Constructs an AbsentTagException with the specified detail message and cause.
     *
     * @param p_exceptionMessage detailed Exception message
     * @param p_exceptionCause detailed cause that causes the current exception
     */
     public AbsentTagException(String p_exceptionMessage, Throwable p_exceptionCause) {
          super(p_exceptionMessage, p_exceptionCause);
     }
}

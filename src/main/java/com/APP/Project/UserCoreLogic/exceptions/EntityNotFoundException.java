package com.APP.Project.UserCoreLogic.exceptions;

/**
 * Custom exception class for entity not found scenarios.
 * This exception is thrown when an entity is not found in the system.
 * It extends the UserCoreLogicException class.
 *
 * @author Rikin Dipakkumar Chauhan
 */
public class EntityNotFoundException extends UserCoreLogicException {

    /**
     * Constructs an EntityNotFoundException with the specified detail message.
     *
     * @param p_exceptionMessage detailed Exception message
     */
    public EntityNotFoundException(String p_exceptionMessage) {
        super(p_exceptionMessage);
    }

    /**
     * Constructs a new EntityNotFoundException with the specified detail message and cause.
     *
     * @param p_exceptionMessage detailed Exception message
     * @param p_exceptionCause detailed cause that causes the current exception
     */
    public EntityNotFoundException(String p_exceptionMessage, Throwable p_exceptionCause) {
        super(p_exceptionMessage, p_exceptionCause);
    }

}

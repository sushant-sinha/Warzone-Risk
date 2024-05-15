package com.APP.Project.UserCoreLogic.exceptions;

/**
 * Represents an exception thrown when an invalid order is encountered within the UserCoreLogic.
 * This exception is typically thrown when an order does not meet certain criteria or requirements.
 *
 * @author Rikin Dipakkumar Chauhan
 * @version 1.0
 */
public class InvalidOrderException extends UserCoreLogicException {
    
    /**
     * Constructs a new InvalidOrderException with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval by the getMessage() method).
     */
    public InvalidOrderException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidOrderException with the specified detail message and cause.
     *
     * @param message The detail message (which is saved for later retrieval by the getMessage() method).
     * @param cause   The cause (which is saved for later retrieval by the getCause() method).
     */
    public InvalidOrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
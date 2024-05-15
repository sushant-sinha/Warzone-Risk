package com.APP.Project.UserCoreLogic.exceptions;

/**
 * Custom exception class representing a situation where a card is not found within the user core logic.
 * This exception is thrown to indicate that a requested card cannot be located.
 *
 * @author Rikin Dipakkumar Chauhan
 * @version 1.0
 */
public class CardNotFoundException extends UserCoreLogicException {

    /**
     * Constructs a new CardNotFoundException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     */
    public CardNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new CardNotFoundException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     * @param cause   the cause (which is saved for later retrieval by the getCause() method).
     */
    public CardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
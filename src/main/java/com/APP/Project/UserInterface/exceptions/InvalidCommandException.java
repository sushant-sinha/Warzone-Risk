package com.APP.Project.UserInterface.exceptions;

/**
 * This Exception shows when user input is invalid.
 *
 * @author Raj Kumar Ramesh
 * @version 1.0
 */
public class InvalidCommandException extends RuntimeException {
    /**
     * Parameterised constructor.
     * To pass the message to super class.
     * @param message Message to be passed to super class.
     */
    public InvalidCommandException(String message) {
        super(message);
    }

    /**
     * Parameterised constructor.
     * To pass message.
     * And throw exception.
     * @param message Message to be passed to super class.
     * @param cause   Cause of the exception to be passed to super class.
     */
    public InvalidCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
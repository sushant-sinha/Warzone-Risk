package com.APP.Project.UserInterface.exceptions;

/**
 * Exception to indicate to user with respect to invalid arguements.
 *
 * @author Raj Kumar Ramesh
 * @version 1.0
 */
public class InvalidArgumentException extends RuntimeException {
    /**
     * Parameterised constructor.
     * Passes message to super class.
     * @param message Message to be passed to super class.
     */
    public InvalidArgumentException(String message) {
        super(message);
    }


    /**
     * Parameterised constructor.
     * To pass message.
     * And throw exception.
     * @param message Message to be passed to super class.
     * @param cause   Cause of the exception.
     */
    public InvalidArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
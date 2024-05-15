package com.APP.Project.UserCoreLogic.exceptions;

/**
 * Exception indicating that the input provided is invalid.
 * 
 * @author Rikin Dipakkumar Chauhan
 * @version 1.0
 */
public class InvalidInputException extends UserCoreLogicException {
    
    /**
     * Constructs an InvalidInputException with the specified detail message.
     * 
     * @param p_message The detail message (which is saved for later retrieval by the getMessage() method)
     */
    public InvalidInputException(String p_message) {
        super(p_message);
    }

    /**
     * Constructs an InvalidInputException with the specified detail message and cause.
     * 
     * @param p_message The detail message (which is saved for later retrieval by the getMessage() method)
     * @param p_cause   The cause (which is saved for later retrieval by the getCause() method)
     */
    public InvalidInputException(String p_message, Throwable p_cause) {
        super(p_message, p_cause);
    }
}
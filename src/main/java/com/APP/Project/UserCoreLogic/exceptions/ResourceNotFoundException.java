package com.APP.Project.UserCoreLogic.exceptions;

/**
 * The ResourceNotFoundException class represents an exception that indicates 
 * a requested resource was not found.
 * 
 * @author Rikin Dipakkumar Chauhan
 * @version 1.0
 */
public class ResourceNotFoundException extends UserCoreLogicException {
    
    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     * 
     * @param p_message the detail message (which is saved for later retrieval by the getMessage() method)
    */
    public ResourceNotFoundException(String p_message) {
        super(p_message);
    }

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message and cause.
     * 
     * @param p_message the detail message (which is saved for later retrieval by the getMessage() method)
     * @param p_cause the cause (which is saved for later retrieval by the getCause() method)
    */    
    public ResourceNotFoundException(String p_message, Throwable p_cause) {
        super(p_message, p_cause);
    }
}

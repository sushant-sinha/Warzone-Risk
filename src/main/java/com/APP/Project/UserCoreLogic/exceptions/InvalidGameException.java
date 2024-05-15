package com.APP.Project.UserCoreLogic.exceptions;

/**
 * This exception represents an invalid game file exception.
 * 
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class InvalidGameException extends UserCoreLogicException {
    
    /**
     * Constructs an InvalidGameException with a default message.
     */
    public InvalidGameException() {
        super("Invalid game file!");
    }

    /**
     * Constructs an InvalidGameException with a specified message.
     * 
     * @param p_message The message explaining the exception.
     */
    public InvalidGameException(String p_message) {
        super(p_message);
    }

    /**
     * Constructs an InvalidGameException from another UserCoreLogicException.
     * 
     * @param p_vmException The UserCoreLogicException to wrap.
     */
    public InvalidGameException(UserCoreLogicException p_vmException) {
        super(p_vmException.getMessage());
    }

    /**
     * Constructs an InvalidGameException with a specified message and cause.
     * 
     * @param message The message explaining the exception.
     * @param cause The cause of the exception.
     */
    public InvalidGameException(String message, Throwable cause) {
        super(message, cause);
    }
}
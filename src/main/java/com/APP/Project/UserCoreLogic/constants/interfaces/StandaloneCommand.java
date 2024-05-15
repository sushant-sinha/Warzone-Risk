package com.APP.Project.UserCoreLogic.constants.interfaces;

import com.APP.Project.UserCoreLogic.exceptions.UserCoreLogicException;

import java.util.List;

/**
 * Defines an interface for commands without argument keys, allowing for zero or more values.
 * Implement this interface for commands that operate without specific keyed arguments.
 *
 * @author Bhoomiben Bhatt
 * @version 1.0
 */
public interface StandaloneCommand {
    /**
     * Executes the command from the UserInterface with provided arguments, which may be empty. This method processes commands
     * that don't require keyed arguments but might have values.
     *
     * @param p_commandValues The list of argument values provided during command execution.
     * @return A string to be displayed to the user as command output.
     * @throws UserCoreLogicException Throws the base class If invalid input is provided or an IOException occurs.
     */
    String execute(List<String> p_commandValues) throws UserCoreLogicException;
}

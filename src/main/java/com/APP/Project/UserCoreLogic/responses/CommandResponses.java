package com.APP.Project.UserCoreLogic.responses;

import java.util.List;

/**
 /**
 * Represents a command interpreted from the <code>UserInterface</code> during the game_state phase.
 *
 * @author Bhoomiben Bhatt
 * @version 1.0
 */
public class CommandResponses {
    /**
     * Represents the user command
     */
    private String d_headCommand;

    /**
     * Value(s) of the head of the command if any
     */
    private List<String> d_commandValues;

    /**
     * Retrieves the command's initial segment.
     *
     * @return head of the command
     */
    public String getHeadCommand() {
        return d_headCommand;
    }

    /**
     * Assigns the initial segment or head of the command.
     *
     * @param p_headCommand head of the command
     */
    public void setHeadCommand(String p_headCommand) {
        d_headCommand = p_headCommand;
    }

    /**
     * Retrieves the values at the beginning of the command, if present.
     *
     * @return The values at the command's head, if any exist.
     */
    public List<String> getCommandValues() {
        return d_commandValues;
    }

    /**
     * Updates the command with new values.
     *
     * @param d_commandValues  The new values to assign to the command.
     */
    public void setCommandValues(List<String> d_commandValues) {
        this.d_commandValues = d_commandValues;
    }

    /**
     * Determines whether the player wishes to stop issuing orders.
     *
     * @return True if the player opts not to issue further orders.
     */
    public boolean isDone() {
        return getHeadCommand().equals("done");
    }
}

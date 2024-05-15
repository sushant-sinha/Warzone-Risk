package com.APP.Project.UserInterface.layouts;

import com.APP.Project.UserInterface.models.PredefinedUserCommands;

import java.util.List;

/**
 * Interface to define the method(s) which should be implemented for various command-layout classes. The command-layout
 * are the classes that define the available commands throughout a game state.
 *
 * @author Raj Kumar Ramesh
 * @version 1.0
 */
public interface CommandLayout {
    /**
     * Retrieves the inherited class's user command list. 
     * The list of user commands generated in the constructor or static block must be included in the inheriting class.
     *
     * @return the list of commands
     */
    public List<PredefinedUserCommands> getUserCommands();
}

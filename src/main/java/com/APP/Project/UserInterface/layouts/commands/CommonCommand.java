package com.APP.Project.UserInterface.layouts.commands;

import com.APP.Project.UserInterface.layouts.CommandLayout;
import com.APP.Project.UserInterface.models.PredefinedUserCommands;
import com.APP.Project.UserInterface.constants.specifications.CommandSpecification;

import java.util.ArrayList;
import java.util.List;

/**
 * This class has all the commands entered by user during the GAME_PLAY game state.
 *
 * @author Raj Kumar Ramesh
 * @version 1.0
 */
public class CommonCommand implements CommandLayout {
    /**
     * The list of user commands entered during GAME_PLAY state of GameState
     */
    List<PredefinedUserCommands> d_userCommands;

    /**
     * Creates the instance of redefinedUserCommands
     * Sets variables and adds to list of user commands.
     */
    public CommonCommand() {
        d_userCommands = new ArrayList<>();

        PredefinedUserCommands l_userCommand;

        // Example of the below command:
        // > loadmap filename
        l_userCommand = new PredefinedUserCommands();
        l_userCommand.setHeadCommand("loadmap");
        l_userCommand.setCommandSpecification(CommandSpecification.CAN_RUN_ALONE_WITH_VALUE);
        l_userCommand.setGamePhaseMethodName("loadMap");
        d_userCommands.add(l_userCommand);

        // Example of the below command:
        // > showmap
        l_userCommand = new PredefinedUserCommands();
        l_userCommand.setHeadCommand("showmap");
        l_userCommand.setCommandSpecification(CommandSpecification.CAN_RUN_ALONE);
        l_userCommand.setGamePhaseMethodName("showMap");
        l_userCommand.setGameEngineCommand(true);
        d_userCommands.add(l_userCommand);

        l_userCommand = new PredefinedUserCommands();
        l_userCommand.setHeadCommand("done");
        l_userCommand.setCommandSpecification(CommandSpecification.CAN_RUN_ALONE);
        l_userCommand.setOrderCommand(true);
        d_userCommands.add(l_userCommand);

        // Example of the below command:
        // > exit
        l_userCommand = new PredefinedUserCommands();
        l_userCommand.setHeadCommand("exit");
        l_userCommand.setCommandSpecification(CommandSpecification.CAN_RUN_ALONE);
        l_userCommand.setGamePhaseMethodName("endGame");
        l_userCommand.setGameEngineCommand(true);
        d_userCommands.add(l_userCommand);
    }

    /**
     * {@inheritDoc}
     *
     * @return Value of the list of user commands for this class.
     */
    @Override
    public List<PredefinedUserCommands> getUserCommands() {
        return this.d_userCommands;
    }
}

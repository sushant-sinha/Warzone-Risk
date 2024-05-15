package com.APP.Project.UserInterface.layouts.commands;

import com.APP.Project.UserInterface.layouts.CommandLayout;
import com.APP.Project.UserInterface.models.CommandLineArgument;
import com.APP.Project.UserInterface.models.PredefinedUserCommands;
import com.APP.Project.UserInterface.constants.specifications.ArgumentSpecification;
import com.APP.Project.UserInterface.constants.specifications.CommandSpecification;

import java.util.ArrayList;
import java.util.List;

/**
 * This class has all the commands entered by user during the MAP_EDITOR game state.
 *
 * @author Raj Kumar Ramesh
 * @version 1.0
 */
public class MapEditorCommand implements CommandLayout {
    /**
    * The list of user commands entered during MAP_EDITOR state of GameState
     */
    List<PredefinedUserCommands> d_userCommands;

    /**
     * Constructor sets the predefined user commands. Commands used to check structure of command enetered by user.
     */
    public MapEditorCommand() {
        d_userCommands = new ArrayList<>();

        // Example of the below command:
        // > editcontinent -add continentID continentvalue -remove continentID
        PredefinedUserCommands l_userCommand = new PredefinedUserCommands();
        l_userCommand.setHeadCommand("editcontinent");
        l_userCommand.setCommandSpecification(CommandSpecification.NEEDS_KEYS);
        l_userCommand.pushCommandArgument(new CommandLineArgument(
                "add",
                2,
                ArgumentSpecification.EQUAL
        ));
        l_userCommand.pushCommandArgument(new CommandLineArgument(
                "remove",
                1,
                ArgumentSpecification.EQUAL
        ));
        l_userCommand.setGamePhaseMethodName("editContinent");
        d_userCommands.add(l_userCommand);

        // Example of the below command:
        // > editcountry -add countryID continentID -remove countryID
        l_userCommand = new PredefinedUserCommands();
        l_userCommand.setHeadCommand("editcountry");
        l_userCommand.setCommandSpecification(CommandSpecification.NEEDS_KEYS);
        l_userCommand.pushCommandArgument(new CommandLineArgument(
                "add",
                2,
                ArgumentSpecification.EQUAL
        ));
        l_userCommand.pushCommandArgument(new CommandLineArgument(
                "remove",
                1,
                ArgumentSpecification.EQUAL
        ));
        l_userCommand.setGamePhaseMethodName("editCountry");
        d_userCommands.add(l_userCommand);

        // Example of the below command:
        // > editneighbor -add countryID neighborcountryID -remove countryID neighborcountryID
        l_userCommand = new PredefinedUserCommands();
        l_userCommand.setHeadCommand("editneighbor");
        l_userCommand.setCommandSpecification(CommandSpecification.NEEDS_KEYS);
        l_userCommand.pushCommandArgument(new CommandLineArgument(
                "add",
                2,
                ArgumentSpecification.EQUAL
        ));
        l_userCommand.pushCommandArgument(new CommandLineArgument(
                "remove",
                2,
                ArgumentSpecification.EQUAL
        ));
        l_userCommand.setGamePhaseMethodName("editNeighbor");
        d_userCommands.add(l_userCommand);

        // Example of the below command:
        // > savemap filename map_type
        l_userCommand = new PredefinedUserCommands();
        l_userCommand.setHeadCommand("savemap");
        l_userCommand.setCommandSpecification(CommandSpecification.CAN_RUN_ALONE_WITH_VALUE);
        l_userCommand.setGamePhaseMethodName("saveMap");
        l_userCommand.setNumOfValues(2);
        d_userCommands.add(l_userCommand);

        // Example of the below command:
        // > editmap filename
        l_userCommand = new PredefinedUserCommands();
        l_userCommand.setHeadCommand("editmap");
        l_userCommand.setCommandSpecification(CommandSpecification.CAN_RUN_ALONE_WITH_VALUE);
        l_userCommand.setGamePhaseMethodName("editMap");
        d_userCommands.add(l_userCommand);

        // Example of the below command:
        // > validatemap
        l_userCommand = new PredefinedUserCommands();
        l_userCommand.setHeadCommand("validatemap");
        l_userCommand.setCommandSpecification(CommandSpecification.CAN_RUN_ALONE);
        l_userCommand.setGamePhaseMethodName("validateMap");
        d_userCommands.add(l_userCommand);

        // Example of the below command:
        // > tournament -M listofmapfiles -P listofplayerstrategies -G numberofgames -D maxnumberofturns
        l_userCommand = new PredefinedUserCommands();
        l_userCommand.setHeadCommand("tournament");
        l_userCommand.setCommandSpecification(CommandSpecification.NEEDS_KEYS);
        l_userCommand.pushCommandArgument(new CommandLineArgument(
                "M",
                1,
                ArgumentSpecification.MIN
        ));
        l_userCommand.pushCommandArgument(new CommandLineArgument(
                "P",
                2,
                ArgumentSpecification.MIN
        ));
        l_userCommand.pushCommandArgument(new CommandLineArgument(
                "G",
                1,
                ArgumentSpecification.EQUAL
        ));
        l_userCommand.pushCommandArgument(new CommandLineArgument(
                "D",
                1,
                ArgumentSpecification.EQUAL
        ));
        l_userCommand.setNumOfValues(4);
        l_userCommand.setCommandKeySpecification(ArgumentSpecification.EQUAL);
        l_userCommand.setGamePhaseMethodName("prepareTournament");
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

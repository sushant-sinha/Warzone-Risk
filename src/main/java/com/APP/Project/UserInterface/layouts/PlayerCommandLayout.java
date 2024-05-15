package com.APP.Project.UserInterface.layouts;

import com.APP.Project.UserInterface.models.PredefinedUserCommands;
import com.APP.Project.UserInterface.exceptions.InvalidCommandException;
import com.APP.Project.UserInterface.layouts.commands.CommonCommand;
import com.APP.Project.UserInterface.layouts.commands.GamePlayCommand;
import com.APP.Project.UserInterface.layouts.commands.MapEditorCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The command-layout classes are mapped to their game states by this class. No instances of the class need to be created in order to use it.
 *
 * @author Raj Kumar Ramesh
 * @version 1.0
 */
public class PlayerCommandLayout {
    /**
     * List of all class in every GameState (a game state) is listed.
     */
    private final static List<PredefinedUserCommands> d_GameCommandLayouts = new ArrayList<>();

    /**
     * The object contains user commands for GAME_PLAY game state.
     */
    private static final CommonCommand COMMON_LAYOUT = new CommonCommand();

    /**
     * The object contains user commands for Map_Editor game state.
     */
    private static final MapEditorCommand MAP_EDITOR_LAYOUT = new MapEditorCommand();

    /**
     * The object contains user commands for GAME_PLAY game state.
     */
    private static final GamePlayCommand GAME_PLAY_LAYOUT = new GamePlayCommand();

    /*
     * Stores the commands according to the game state
     */
    static {
        d_GameCommandLayouts.addAll(COMMON_LAYOUT.getUserCommands());
        d_GameCommandLayouts.addAll(MAP_EDITOR_LAYOUT.getUserCommands());
        d_GameCommandLayouts.addAll(GAME_PLAY_LAYOUT.getUserCommands());
    }

    /**
     * Gets matched the user command It decides the which list of predefined command using the game state Then it
     * matches the user command with the head of the command provided
     *
     * @param p_headOfCommand head of the command which needs to be matched 
     * @return Value of user command that matched with p_headOfCommand
     * @throws InvalidCommandException If no command found thow exception.
     */
    public static PredefinedUserCommands matchAndGetUserCommand(String p_headOfCommand) throws InvalidCommandException {
        // Gets the list of command from the layout, and then it is being streamed over to filter the list
        return PlayerCommandLayout.findFirstByHeadOfCommand(p_headOfCommand);
    }

    /**
     * Uses the head of the command headOfCommand data member to get the list of matched PredefinedUserCommands.
     *
     * @param p_headOfCommand Value of head command tha needs to be matched
     * @return Value of found PredefinedUserCommands.
     */
    private static List<PredefinedUserCommands> findByHeadOfCommand(String p_headOfCommand) {
        return d_GameCommandLayouts
                .stream().filter((userCommand) ->
                        userCommand.getHeadCommand().equals(p_headOfCommand)
                ).collect(Collectors.toList());
    }

    /**
     * Uses the head of the command headOfCommand data member to find the first matched PredefinedUserCommands.
     *
     * @param p_headOfCommand Value of head command that needs to be matched
     * @return Value of found PredefinedUserCommands.
     * @throws InvalidCommandException If command not found throw exception.
     */
    private static PredefinedUserCommands findFirstByHeadOfCommand(String p_headOfCommand) {
        try {
            return PlayerCommandLayout.findByHeadOfCommand(p_headOfCommand).get(0);
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            throw new InvalidCommandException("Unrecognized command!");
        }
    }

    /**
     * Gets the instance having the user command list for MAP_EDITOR game state.
     *
     * @return Value of instance having the list of MAP_EDITOR user commands.
     */
    private static MapEditorCommand getMapEditorLayout() {
        return PlayerCommandLayout.MAP_EDITOR_LAYOUT;
    }

    /**
     * Gets the instance having the user command list for GAME_PLAY game state.
     *
     * @return Value of instance having the list of GAME_PLAY user commands.
     */
    private static GamePlayCommand getGamePlayLayout() {
        return PlayerCommandLayout.GAME_PLAY_LAYOUT;
    }
}

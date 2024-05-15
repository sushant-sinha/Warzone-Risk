package com.APP.Project.UserCoreLogic.phases;

import com.APP.Project.UserCoreLogic.GameEngine;
import com.APP.Project.UserCoreLogic.common.services.LoadGameService;
import com.APP.Project.UserCoreLogic.common.services.SaveGameService;
import com.APP.Project.UserCoreLogic.exceptions.InvalidCommandException;
import com.APP.Project.UserCoreLogic.exceptions.UserCoreLogicException;
import com.APP.Project.UserCoreLogic.gamePlay.services.*;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;
import com.APP.Project.UserCoreLogic.map_features.adapters.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Core component for managing game states, similar to the "State" design pattern.
 * This setup is particularly for the game Risk, where each state represents a phase in the game such as setting up the game, playing, and ending it. Some states are simple while others are more complex and hierarchical.
 *  
 * @author Raj Kumar Ramesh
 * @version 1.0
 */
public abstract class Phase {
    GameEngine d_gameEngine; // Links to the game's main engine to switch between phases.

    /**
     * Constructor to initialize the phase with a game engine reference.
     * 
     * @param p_gameEngine The game's engine instance.
     */
    Phase(GameEngine p_gameEngine) {
        d_gameEngine = p_gameEngine;
    }

    /**
     * Prepares a tournament mode based on given settings.
     * 
     * @param p_arguments Settings for the tournament.
     * @return A message indicating the operation's outcome.
     * @throws UserCoreLogicException If there's an issue setting up the tournament.
     */
    abstract public String prepareTournament(List<Map<String, List<String>>> p_arguments) throws UserCoreLogicException;

    /**
     * Loads a game map from a specified file.
     * 
     * @param p_arguments Filename to load the map from.
     * @return A message about the load outcome.
     * @throws UserCoreLogicException If the map can't be loaded.
     */
    abstract public String loadMap(List<String> p_arguments) throws UserCoreLogicException;

    /**
     * Displays the current game map.
     * 
     * @param p_arguments Not used.
     * @return A message indicating the operation's outcome.
     * @throws UserCoreLogicException If displaying the map fails.
     */
    abstract public String showMap(List<String> p_arguments) throws UserCoreLogicException;

    /**
     * Edits the game map based on provided filename.
     * 
     * @param p_arguments Filename for editing.
     * @return A message indicating the operation's outcome.
     * @throws UserCoreLogicException If editing fails.
     * @throws IOException If an I/O error occurs.
     */
    abstract public String editMap(List<String> p_arguments) throws UserCoreLogicException, IOException;

    /**
     * Edits continents on the map.
     * 
     * @param l_serviceType The specific editing action.
     * @param p_arguments Details for the edit.
     * @return A message indicating the outcome.
     * @throws UserCoreLogicException If the edit fails.
     */
    abstract public String editContinent(String l_serviceType, List<String> p_arguments) throws UserCoreLogicException;

    /**
     * Edits countries within continents on the map.
     * 
     * @param l_serviceType The specific editing action.
     * @param p_arguments Details for the edit.
     * @return A message indicating the outcome.
     * @throws UserCoreLogicException If the edit fails.
     */
    abstract public String editCountry(String l_serviceType, List<String> p_arguments) throws UserCoreLogicException;

    /**
     * Edits neighboring relationships between countries on the map.
     * 
     * @param l_serviceType The specific editing action.
     * @param p_arguments Details for the edit.
     * @return A message indicating the outcome.
     * @throws UserCoreLogicException If the edit fails.
     */
    abstract public String editNeighbor(String l_serviceType, List<String> p_arguments) throws UserCoreLogicException;

    /**
     * Checks if the map is valid and playable.
     * 
     * @param p_arguments Not used.
     * @return A message indicating the validation result.
     * @throws UserCoreLogicException If validation fails.
     */
    abstract public String validateMap(List<String> p_arguments) throws UserCoreLogicException;

    /**
     * Saves the current map to a file.
     * 
     * @param p_arguments Filename to save the map to.
     * @return A message indicating the save outcome.
     * @throws UserCoreLogicException If saving fails.
     * @throws IOException If an I/O error occurs.
     */
    abstract public String saveMap(List<String> p_arguments)

 throws UserCoreLogicException, IOException;

    /**
     * Adds players to the game.
     * 
     * @param l_serviceType The type of player setting.
     * @param p_arguments Player details.
     * @return A message indicating the operation's outcome.
     * @throws UserCoreLogicException If adding players fails.
     */
    abstract public String setPlayers(String l_serviceType, List<String> p_arguments) throws UserCoreLogicException;

    /**
     * Assigns countries to players at the start of the game.
     * 
     * @param p_arguments Not used.
     * @return A message indicating the operation's outcome.
     * @throws UserCoreLogicException If the assignment fails.
     */
    abstract public String assignCountries(List<String> p_arguments) throws UserCoreLogicException;

    /**
     * Reinforces players' armies. Called during the main play loop.
     * 
     * @throws UserCoreLogicException If reinforcement fails.
     */
    abstract public void reinforce() throws UserCoreLogicException;

    /**
     * Players issue orders. Called during the main play loop.
     * 
     * @throws UserCoreLogicException If issuing orders fails.
     */
    abstract public void issueOrder() throws UserCoreLogicException;

    /**
     * Executes players' orders. Called during the main play loop.
     * 
     * @throws UserCoreLogicException If execution fails.
     */
    abstract public void fortify() throws UserCoreLogicException;

    /**
     * Saves the current game state to a file.
     * 
     * @param p_arguments Path to the save file.
     * @return A message indicating the save outcome.
     * @throws UserCoreLogicException If saving fails.
     */
    public String saveGame(List<String> p_arguments) throws UserCoreLogicException {
        SaveGameService l_saveGameService = new SaveGameService();
        return l_saveGameService.execute(p_arguments);
    }

    /**
     * Loads a game state from a file.
     * 
     * @param p_arguments Path to the load file.
     * @return A message indicating the load outcome.
     * @throws UserCoreLogicException If loading fails.
     */
    public String loadGame(List<String> p_arguments) throws UserCoreLogicException {
        LoadGameService l_loadGameService = new LoadGameService();
        return l_loadGameService.execute(p_arguments);
    }

    /**
     * Ends the game. Called when exiting the main play loop.
     * 
     * @param p_arguments Not used.
     * @throws UserCoreLogicException If the operation fails.
     */
    abstract public void endGame(List<String> p_arguments) throws UserCoreLogicException;

    /**
     * Moves the game to the next phase.
     * 
     * @throws UserCoreLogicException If the transition fails.
     */
    abstract public void nextState() throws UserCoreLogicException;

    /**
     * Handles invalid commands in any game phase.
     * 
     * @return Always throws an exception to indicate an invalid command.
     * @throws UserCoreLogicException Signifies an invalid command was received.
     */
    public String invalidCommand() throws UserCoreLogicException {
        throw new InvalidCommandException("Invalid command!");
    }

    /**
     * Dynamically invokes a method on a target object, using the method's name and arguments provided.
     * 
     * @param p_target Object to invoke the method on.
     * @param p_methodName Name of the method to invoke.
     * @param p_argValues Arguments for the method call.
     * @return The method's return value.
     * @throws UserCoreLogicException If the method call fails for any reason, including if the method does not exist or is inaccessible.
     */
    public String invokeMethod(Object p_target, String p_methodName, List<String> p_argValues) throws UserCoreLogicException {
        Class<?>[] l_valueTypes = new Class[p_argValues.size()];
        Object[] l_values = p_argValues.toArray();
        for (int l_argIndex = 0; l_argIndex < p_argValues.size(); l_argIndex++) {
            l_valueTypes[l_argIndex] = String.class;
        }

        try {
            Method l_methodReference = p_target.getClass().getMethod(p_methodName, l_valueTypes);
            return (String) l_methodReference.invoke(p_target, l_values);
        } catch (InvocationTargetException p_invocationTargetException) {
            LogEntryBuffer.getLogger().dataChanged("error", p_invocationTargetException.getCause().getMessage());
            throw new UserCoreLogicException(p_invocationTargetException.getCause().getMessage());
        } catch (NoSuchMethodException | IllegalAccessException p_e) {
            LogEntryBuffer.getLogger().dataChanged("error", "Invalid command!");
            this.invalidCommand();
        }
        return null;
    }
}

package com.APP.Project.UserCoreLogic.phases;

import com.APP.Project.UserCoreLogic.GameEngine;
import com.APP.Project.UserCoreLogic.exceptions.InvalidMapException;
import com.APP.Project.UserCoreLogic.exceptions.UserCoreLogicException;
import com.APP.Project.UserCoreLogic.map_features.adapters.LoadConquestMapService;
import com.APP.Project.UserCoreLogic.map_features.adapters.ShowMapAdapter;
import com.APP.Project.UserCoreLogic.Utility.FileValidationUtil;
import com.APP.Project.UserCoreLogic.Utility.FindFilePathUtil;
import com.APP.Project.UserCoreLogic.exceptions.InvalidInputException;
import com.APP.Project.UserCoreLogic.map_features.adapters.LoadMapAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * This abstract class represents the phase where map editing operations can be performed.
 * It extends the Phase class and provides implementations for certain methods specific to map editing.
 *
 * @author Raj Kumar Ramesh
 * @version 1.0
 */
public abstract class MapEditor extends Phase {
    /**
     * Constructs a new MapEditor object with the specified GameEngine.
     *
     * @param p_gameEngine the GameEngine instance associated with this MapEditor
     */
    MapEditor(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    /**
     * Loads a map using the specified list of arguments.
     * It sets the game phase to PlaySetup after loading the map.
     *
     * @param p_arguments a list of arguments needed to load the map
     * @return a string indicating the result of the load operation
     * @throws UserCoreLogicException if an error occurs during the map loading process
     */
    @Override
    public String loadMap(List<String> p_arguments) throws UserCoreLogicException {
        // Resolve file path using absolute path of user data directory.
        String l_resolvedPathToFile = FindFilePathUtil.resolveFilePath(p_arguments.get(0));
        if (new File(l_resolvedPathToFile).exists()) {
            // Try to retrieve the file
            FileValidationUtil.retrieveMapFile(l_resolvedPathToFile);
            try (BufferedReader l_reader = new BufferedReader(new FileReader(l_resolvedPathToFile));) {
                // Will throw exception if the file path is not valid

                String l_currentLine;
                // If the line is empty, go to next.
                while ((l_currentLine = l_reader.readLine()) != null) {
                    if (!l_currentLine.trim().isEmpty()) {
                        break;
                    }
                }
                if (l_currentLine != null && l_currentLine.startsWith("[")) {
                    final String l_substring = l_currentLine.substring(l_currentLine.indexOf("[") + 1, l_currentLine.indexOf("]"));
                    if (l_substring.equalsIgnoreCase("continents")) {
                        LoadMapAdapter l_loadMapService = new LoadMapAdapter();
                        d_gameEngine.setGamePhase(new PlaySetup(d_gameEngine));
                        return l_loadMapService.execute(p_arguments);
                    } else if (l_substring.equalsIgnoreCase("Map")) {
                        LoadConquestMapService l_loadConquestMapService = new LoadConquestMapService();
                        d_gameEngine.setGamePhase(new PlaySetup(d_gameEngine));
                        return l_loadConquestMapService.execute(p_arguments);
                    } else {
                        throw new InvalidMapException("Unrecognised map file!");
                    }
                } else {
                    throw new InvalidMapException("Invalid map file!");
                }
            } catch (IOException p_ioException) {
                throw new UserCoreLogicException("Error while processing the file!");
            }
        } else {
            throw new InvalidInputException("Map file doesn't exists.");
        }
    }

    /**
     * Sets players for the game with the specified service type and arguments.
     *
     * @param p_serviceType the type of service for setting players
     * @param p_arguments   the arguments needed for setting players
     * @return a message indicating the invalidity of the command
     */
    @Override
    public String setPlayers(String p_serviceType, List<String> p_arguments) throws UserCoreLogicException {
        return invalidCommand();
    }

    /**
     * Assigns countries to players using the specified arguments.
     *
     * @param p_arguments the arguments needed for assigning countries
     * @return a message indicating the invalidity of the command
     */
    @Override
    public String assignCountries(List<String> p_arguments) throws UserCoreLogicException {
        return invalidCommand();
    }

    /**
     * Reinforces the game.
     *
     * @throws UserCoreLogicException if an error occurs during reinforcement
     */
    @Override
    public void reinforce() throws UserCoreLogicException {
        invalidCommand();
    }

    /**
     * Issues orders for the game.
     *
     * @throws UserCoreLogicException if an error occurs during issuing orders
     */
    @Override
    public void issueOrder() throws UserCoreLogicException {
        invalidCommand();
    }

    /**
     * Fortifies the game.
     *
     * @throws UserCoreLogicException if an error occurs during fortification
     */
    @Override
    public void fortify() throws UserCoreLogicException {
        invalidCommand();
    }

    /**
     * Ends the game using the specified arguments.
     *
     * @param p_arguments the arguments needed for ending the game
     * @throws UserCoreLogicException if an error occurs during the end game operation
     */
    @Override
    public void endGame(List<String> p_arguments) throws UserCoreLogicException {
        d_gameEngine.setGamePhase(new End(d_gameEngine));
    }

    /**
     * Shows the map using the specified arguments.
     *
     * @param p_arguments the arguments needed for showing the map
     * @return the map as a string
     * @throws UserCoreLogicException if an error occurs during the show map operation
     */
    @Override
    public String showMap(List<String> p_arguments) throws UserCoreLogicException {
        ShowMapAdapter l_showMapService = new ShowMapAdapter();
        return l_showMapService.execute(p_arguments);
    }
}

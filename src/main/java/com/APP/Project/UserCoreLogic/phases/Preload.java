package com.APP.Project.UserCoreLogic.phases;

import com.APP.Project.UserCoreLogic.GameEngine;
import com.APP.Project.UserCoreLogic.TournamentEngine;
import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.constants.enums.FileType;
import com.APP.Project.UserCoreLogic.constants.enums.StrategyType;
import com.APP.Project.UserCoreLogic.exceptions.InvalidArgumentException;
import com.APP.Project.UserCoreLogic.map_features.adapters.EditMapService;
import com.APP.Project.UserCoreLogic.Utility.FileValidationUtil;
import com.APP.Project.UserCoreLogic.Utility.FindFilePathUtil;
import com.APP.Project.UserCoreLogic.game_entities.Player;
import com.APP.Project.UserCoreLogic.exceptions.InvalidMapException;
import com.APP.Project.UserCoreLogic.exceptions.UserCoreLogicException;
import com.APP.Project.UserCoreLogic.map_features.adapters.EditConquestMapService;
import com.APP.Project.UserCoreLogic.map_features.adapters.EditMapAdapter;
import com.APP.Project.UserCoreLogic.map_features.adapters.ValidateMapAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * This class represents the Preload phase of map editing in the game's core logic.
 * It extends MapEditor and is responsible for preloading the map.
 *
 * @author Raj Kumar Ramesh
 * @version 1.0
 */
public class Preload extends MapEditor {
    /**
     * Constructs a Preload object with the given GameEngine.
     *
     * @param p_gameEngine the GameEngine instance
     */
    public Preload(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    /**
     * This method loads the map file which can be either Warzone file or Conquest map file. It loads the file depending
     * upon the file content.
     *
     * @param p_arguments Contains the filename.
     * @return Value of string acknowledging user that the file is loaded or not.
     * @throws UserCoreLogicException Throws if error occurs in VM Engine operation.
     */
    @Override
    public String prepareTournament(List<Map<String, List<String>>> p_arguments) throws UserCoreLogicException {
        TournamentEngine l_tournamentEngine = UserCoreLogic.TOURNAMENT_ENGINE();
        for (Map<String, List<String>> l_argument : p_arguments) {
            if (l_argument.containsKey("M")) {
                // Save the list of provided map files.
                l_tournamentEngine.setMapFileList(l_argument.get("M"));
            } else if (l_argument.containsKey("P")) {
                List<String> l_playerStrategies = l_argument.get("P");
                for (String l_playerStrategy : l_playerStrategies) {
                    try {
                        StrategyType l_strategyType = StrategyType.valueOf(l_playerStrategy.toUpperCase());
                        if (l_strategyType == StrategyType.HUMAN) {
                            throw new InvalidArgumentException("Strategy cannot be `human`!");
                        }
                        l_tournamentEngine.addPlayer(new Player(l_playerStrategy, l_strategyType));
                    } catch (IllegalArgumentException p_e) {
                        throw new InvalidArgumentException("Strategy type is invalid!");
                    }
                }
            } else if (l_argument.containsKey("G")) {
                try {
                    int l_numberOfGamesValue = Integer.parseInt(l_argument.get("G").get(0));
                    l_tournamentEngine.setNumberOfGames(l_numberOfGamesValue);
                } catch (IndexOutOfBoundsException p_e) {
                    throw new InvalidArgumentException("Number of games not specified!");
                } catch (NumberFormatException p_exception) {
                    throw new InvalidArgumentException("Number of games is in invalid format!");
                }
            } else if (l_argument.containsKey("D")) {
                try {
                    int l_maxNumberOfTurns = Integer.parseInt(l_argument.get("D").get(0));
                    l_tournamentEngine.setMaxNumberOfTurns(l_maxNumberOfTurns);
                } catch (IndexOutOfBoundsException p_e) {
                    throw new InvalidArgumentException("Number of maximum turns not specified!");
                } catch (NumberFormatException p_exception) {
                    throw new InvalidArgumentException("Number of maximum turns is in invalid format!");
                }
            }
        }
        // If no error occurred during preparing the tournament, start it.
        this.d_gameEngine.setGamePhase(new Reinforcement(d_gameEngine));
        l_tournamentEngine.onStart(false);
        return "";
    }

    /**
     * Edits the map with the provided arguments.
     *
     * @param p_arguments the list of arguments for map editing
     * @return a string representing the result of the map editing operation
     * @throws UserCoreLogicException if an error occurs during map editing
     */
    @Override
    public String editMap(List<String> p_arguments) throws UserCoreLogicException, IOException {
        String l_returnValue;

        // Resolve file path using absolute path of user data directory.
        String l_resolvedPathToFile = FindFilePathUtil.resolveFilePath(p_arguments.get(0));

        EditMapService l_editMapService;
        if (new File(l_resolvedPathToFile).exists()) {
            // Try to retrieve the file
            FileValidationUtil.retrieveFile(l_resolvedPathToFile, FileType.MAP);
            // Will throw exception if the file path is not valid
            BufferedReader l_reader = new BufferedReader(new FileReader(l_resolvedPathToFile));
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
                    l_editMapService = new EditMapService();
                } else if (l_substring.equalsIgnoreCase("Map")) {
                    l_editMapService = new EditMapAdapter(new EditConquestMapService());
                } else {
                    throw new InvalidMapException("Unrecognised map file!");
                }
            } else {
                throw new InvalidMapException("Invalid map file!");
            }
        } else {
            // Will create a new file if it doesn't exists.
            l_editMapService = new EditMapService();
        }
        l_returnValue = l_editMapService.execute(p_arguments);
        d_gameEngine.setGamePhase(new PostLoad(d_gameEngine));
        return l_returnValue;
    }

    /**
     * Edits the continent with the specified service type and arguments.
     * This operation is not supported in the Preload phase.
     *
     * @param l_serviceType the service type for continent editing
     * @param p_arguments   the list of arguments for continent editing
     * @return a string indicating that the command is invalid
     */
    @Override
    public String editContinent(String l_serviceType, List<String> p_arguments) throws UserCoreLogicException {
        return this.invalidCommand();
    }

    /**
     * Edits the country with the specified service type and arguments.
     * This operation is not supported in the Preload phase.
     *
     * @param l_serviceType the service type for country editing
     * @param p_arguments   the list of arguments for country editing
     * @return a string indicating that the command is invalid
     */
    @Override
    public String editCountry(String l_serviceType, List<String> p_arguments) throws UserCoreLogicException {
        return this.invalidCommand();
    }

    /**
     * Edits the neighbor with the specified service type and arguments.
     * This operation is not supported in the Preload phase.
     *
     * @param l_serviceType the service type for neighbor editing
     * @param p_arguments   the list of arguments for neighbor editing
     * @return a string indicating that the command is invalid
     */
    @Override
    public String editNeighbor(String l_serviceType, List<String> p_arguments) throws UserCoreLogicException {
        return this.invalidCommand();
    }

    /**
     * Validates the map with the provided arguments.
     *
     * @param p_arguments the list of arguments for map validation
     * @return a string representing the result of the map validation operation
     * @throws UserCoreLogicException if an error occurs during map validation
     */
    @Override
    public String validateMap(List<String> p_arguments) throws UserCoreLogicException {
        ValidateMapAdapter l_validateMapService = new ValidateMapAdapter();
        return l_validateMapService.execute(p_arguments);
    }

    /**
     * Saves the map with the provided arguments.
     * This operation is not supported in the Preload phase.
     *
     * @param p_arguments the list of arguments for map saving
     * @return a string indicating that the command is invalid
     * @throws UserCoreLogicException if an error occurs during map saving
     */
    @Override
    public String saveMap(List<String> p_arguments) throws UserCoreLogicException {
        return invalidCommand();
    }

    /**
     * Moves to the next state.
     *
     * @throws UserCoreLogicException indicating that the map hasn't been loaded yet
     */
    @Override
    public void nextState() throws UserCoreLogicException {
        throw new UserCoreLogicException("Map hasn't been loaded.");
    }
}

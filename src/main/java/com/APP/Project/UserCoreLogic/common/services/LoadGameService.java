package com.APP.Project.UserCoreLogic.common.services;

import com.APP.Project.UserCoreLogic.GameEngine;
import com.APP.Project.UserCoreLogic.Utility.FileValidationUtil;
import com.APP.Project.UserCoreLogic.Utility.FindFilePathUtil;
import com.APP.Project.UserCoreLogic.constants.enums.FileType;
import com.APP.Project.UserCoreLogic.constants.interfaces.StandaloneCommand;
import com.APP.Project.UserCoreLogic.exceptions.UserCoreLogicException;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * This class represents a service for loading game states from files.
 *
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class LoadGameService implements StandaloneCommand {
    private LogEntryBuffer d_logEntryBuffer = LogEntryBuffer.getLogger();

    /**
     * Loads the game state from a JSON object.
     *
     * @param p_targetJSON The JSON object representing the game state.
     * @return A string indicating the success of the operation.
     * @throws UserCoreLogicException If there is an error during the game loading process.
     */
    public String loadGameState(JSONObject p_targetJSON) throws UserCoreLogicException {
        GameEngine.fromJSON(p_targetJSON);
        return "Game loaded successfully";
    }

    @Override
    public String execute(List<String> p_commandValues) throws UserCoreLogicException {
        // Check if the file has the required extension
        FileValidationUtil.checksIfFileHasRequiredExtension(p_commandValues.get(0), FileType.GAME);

        // Retrieve the target file
        File l_targetFile = FileValidationUtil.retrieveGameFile(
                FindFilePathUtil.resolveFilePath(
                        p_commandValues.get(0)
                ));
        StringBuilder l_fileContentBuilder = new StringBuilder();
        try (BufferedReader l_bufferedReader = new BufferedReader(new FileReader(l_targetFile))) {
            String l_currentLine;
            // Read the file content
            while ((l_currentLine = l_bufferedReader.readLine()) != null) {
                l_fileContentBuilder.append(l_currentLine);
            }
        } catch (IOException p_ioException) {
            throw new UserCoreLogicException(String.format("Error while loading the game file %s!", p_commandValues.get(0)));
        }

        // Log the event
        d_logEntryBuffer.dataChanged("loadgame", "Game loaded from file: " + p_commandValues.get(0));

        // Load the game state from the file content
        return this.loadGameState(new JSONObject(l_fileContentBuilder.toString()));


    }
}

package com.APP.Project.UserCoreLogic.common.services;

import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.constants.interfaces.StandaloneCommand;
import com.APP.Project.UserCoreLogic.Utility.FileValidationUtil;
import com.APP.Project.UserCoreLogic.exceptions.InvalidCommandException;
import com.APP.Project.UserCoreLogic.exceptions.UserCoreLogicException;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;
import com.APP.Project.UserCoreLogic.Utility.FindFilePathUtil;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * SaveGameService class provides functionality to save the game state to a file.
 * This class implements the StandaloneCommand interface.
 *
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class SaveGameService implements StandaloneCommand {
    private JSONObject d_currentGameEngine;
    private final LogEntryBuffer d_logEntryBuffer;

    /**
     * Constructs a SaveGameService object initializing the log entry buffer.
     */
    public SaveGameService() {
        d_currentGameEngine = new JSONObject();
        d_logEntryBuffer = LogEntryBuffer.getLogger();
    }

    /**
     * Converts the game state to JSON.
     */
    public void toJSON() {
        d_currentGameEngine = UserCoreLogic.getGameEngine().toJSON();
    }

    /**
     * Retrieves the JSON data of the game engine.
     *
     * @return JSON object representing the game engine state.
     */
    public JSONObject getGameEngineJSONData() {
        return d_currentGameEngine;
    }

    /**
     * Executes the save game command.
     *
     * @param p_commandValues List of command values, expects the file name.
     * @return Success message if the game is saved successfully.
     * @throws UserCoreLogicException if there is an error during the saving process.
     */
    @Override
    public String execute(List<String> p_commandValues) throws UserCoreLogicException {
        if (p_commandValues.size() <= 0) {
            throw new InvalidCommandException("Please provide the file name!");
        }
        this.toJSON();

        File l_targetFile = FileValidationUtil.retrieveGameFile(
                FindFilePathUtil.resolveFilePath(
                        p_commandValues.get(0).concat(".").concat(FileValidationUtil.getGameExtension())
                ));

        try (Writer l_writer = new FileWriter(l_targetFile)) {
            l_writer.write(this.getGameEngineJSONData().toString(4));
            d_logEntryBuffer.dataChanged("savegame", "Game saved successfully with filename: "+p_commandValues.get(0));
            return "Game saved successfully!";
        } catch (IOException p_ioException) {
            throw new UserCoreLogicException("Error in file saving!");
        }
    }
}


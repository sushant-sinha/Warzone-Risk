package com.APP.Project.UserCoreLogic.map_features.adapters;

import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.constants.interfaces.StandaloneCommand;
import com.APP.Project.UserCoreLogic.game_entities.Continent;
import com.APP.Project.UserCoreLogic.game_entities.Country;
import com.APP.Project.UserCoreLogic.exceptions.InvalidInputException;
import com.APP.Project.UserCoreLogic.exceptions.UserCoreLogicException;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;
import com.APP.Project.UserCoreLogic.map_features.MapEditorEngine;
import com.APP.Project.UserCoreLogic.Utility.FileValidationUtil;
import com.APP.Project.UserCoreLogic.Utility.FindFilePathUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class represents the service responsible for saving the map to a file.
 * It implements the StandaloneCommand interface.
 *
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class SaveMapService implements StandaloneCommand {
    
    private final MapEditorEngine d_mapEditorEngine;
    private final LogEntryBuffer d_logEntryBuffer;

    /**
     * Constructs a SaveMapService object.
     */
    public SaveMapService() {
        d_mapEditorEngine = UserCoreLogic.getGameEngine().getMapEditorEngine();
        d_logEntryBuffer = LogEntryBuffer.getLogger();
    }

    /**
     * Saves the map to a file.
     *
     * @param p_fileObject The file object to which the map will be saved.
     * @return A message indicating the status of the operation.
     * @throws InvalidInputException if an error occurs while saving the file.
     */
    public String saveToFile(File p_fileObject) throws InvalidInputException {
        try (Writer l_writer = new FileWriter(p_fileObject)) {
            l_writer.write("[" + "Continents" + "]\n");

            for (Continent continents : d_mapEditorEngine.getContinentList()) {
                l_writer.write(continents.getContinentName() + " " + continents.getContinentControlValue() + "\n");
            }

            l_writer.write("\n[" + "Countries" + "]\n");

            for (Country country : d_mapEditorEngine.getCountryList()) {
                l_writer.write(country.getCountryId() + " " + country.getCountryName() + " " + country.getContinent().getContinentId() + "\n");
            }

            l_writer.write("\n[" + "borders" + "]\n");

            for (Map.Entry<Integer, Set<Integer>> entry : d_mapEditorEngine.getCountryNeighbourMap().entrySet()) {
                int key = entry.getKey();
                Set<Integer> neighbour = entry.getValue();
                l_writer.write(key + " ");
                for (Integer a : neighbour) {
                    l_writer.write(a + " ");
                }
                l_writer.write("\n");
            }
            
            d_mapEditorEngine.initialise();

            
            String l_fileName = p_fileObject.getName();
            int l_index = l_fileName.lastIndexOf('\\');
            String l_loggingMessage = l_fileName.substring(l_index + 1) + " saved successfully!\n";
            d_logEntryBuffer.dataChanged("savemap", l_loggingMessage);

            return "File saved successfully";
        } catch (IOException p_ioException) {
            throw new InvalidInputException("Error while saving the file!");
        }
    }

    /**
     * Executes the save map command.
     *
     * @param p_commandValues List of command values.
     * @return The result of the execution.
     * @throws UserCoreLogicException If an error occurs during execution.
     */
    @Override
    public String execute(List<String> p_commandValues) throws
            UserCoreLogicException {
        
        ValidateMapAdapter l_validateObj = new ValidateMapAdapter();
        l_validateObj.execute(null, "savemap");

        
        return saveToFile(FileValidationUtil.retrieveMapFile(FindFilePathUtil.resolveFilePath(p_commandValues.get(0))));
    }
}

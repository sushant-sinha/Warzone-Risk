package com.APP.Project.UserCoreLogic.map_features.adapters;

import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.constants.interfaces.StandaloneCommand;
import com.APP.Project.UserCoreLogic.game_entities.Continent;
import com.APP.Project.UserCoreLogic.game_entities.Country;
import com.APP.Project.UserCoreLogic.exceptions.UserCoreLogicException;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;
import com.APP.Project.UserCoreLogic.map_features.MapEditorEngine;
import com.APP.Project.UserCoreLogic.Utility.FileValidationUtil;
import com.APP.Project.UserCoreLogic.Utility.FindFilePathUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;

/**
 * SaveConquestMapService class is used to save the conquest map.
 * It implements StandaloneCommand interface.
 * It saves the conquest map and validates it.
 * 
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class SaveConquestMapService implements StandaloneCommand {
    /**
     * Map editor engine object.
     */
    private final MapEditorEngine d_mapEditorEngine;
    private final LogEntryBuffer d_logEntryBuffer;

    /**
     * Constructor of SaveConquestMapService.
     */
    public SaveConquestMapService() {
        d_mapEditorEngine = UserCoreLogic.getGameEngine().getMapEditorEngine();
        d_logEntryBuffer = LogEntryBuffer.getLogger();
    }

    /**
     * Saves the map to the file.
     *
     * @param p_fileObject File object.
     * @return Response of the command.
     * @throws UserCoreLogicException Throws if error in writing to file.
     */
    public String saveToFile(File p_fileObject) throws UserCoreLogicException {
        HashMap<String, String> l_mapDetails;

        try (Writer l_writer = new FileWriter(p_fileObject)) {
            l_writer.write("[" + "Map" + "]\n");
            l_mapDetails = d_mapEditorEngine.getMapDetails();

            if (l_mapDetails != null) {
                for (String l_name : l_mapDetails.keySet()) {
                    String l_key = l_name;
                    String l_value = l_mapDetails.get(l_name);
                    l_writer.write(l_key + "=" + l_value + "\n");
                }
            } else {
                l_writer.write("author=Iceworm72\n");
                l_writer.write("image=002_I72_X-29.bmp\n");
                l_writer.write("wrap=no\n");
                l_writer.write("scroll=horizontalp\n");
                l_writer.write("warn=no\n");
            }

            l_writer.write("\n[" + "Continents" + "]\n");

            for (Continent l_continents : d_mapEditorEngine.getContinentList()) {
                l_writer.write(l_continents.getContinentName() + "=" + l_continents.getContinentControlValue() + "\n");
            }

            l_writer.write("\n[" + "Territories" + "]\n");

            for (Country l_country : d_mapEditorEngine.getCountryList()) {
                String l_join = "";
                l_writer.write(l_country.getCountryName() + "," + l_country.getXCoordinate() + "," + l_country.getYCoordinate() + "," + l_country.getContinent().getContinentName());
                for (Country l_neighbor : l_country.getNeighbourCountries()) {
                    l_join = String.join(",", l_join, l_neighbor.getCountryName());
                }
                l_writer.write(l_join + "\n");
            }
            return "File saved successfully";
        } catch (IOException p_ioException) {
            throw new UserCoreLogicException("Error while saving the file!");
        }
    }

    /**
     * This method is used to save the conquest map.
     *
     * @param p_commandValues List of command values.
     * @return Response of the command.
     * @throws UserCoreLogicException Throws if error occurs in User Core Logic Engine operation.
     */
    @Override
    public String execute(List<String> p_commandValues) throws UserCoreLogicException {
        return saveToFile(FileValidationUtil.retrieveMapFile(FindFilePathUtil.resolveFilePath(p_commandValues.get(0))));
    }
}

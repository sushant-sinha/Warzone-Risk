package com.APP.Project.UserCoreLogic.map_features.adapters;

import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.constants.interfaces.StandaloneCommand;
import com.APP.Project.UserCoreLogic.exceptions.*;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;
import com.APP.Project.UserCoreLogic.Utility.FindFilePathUtil;

import java.util.List;

/**
 * LoadMapAdapter class is used to load the map.
 * It implements StandaloneCommand interface.
 * It loads the map and validates it.
 * 
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class LoadMapAdapter implements StandaloneCommand {
    private LogEntryBuffer d_logEntryBuffer = LogEntryBuffer.getLogger();

    /**
     * This method is used to load the map.
     * 
     * @param p_commandValues List of command values.
     * @return Response of the command.
     * @throws InvalidMapException Invalid map exception.
     * @throws ResourceNotFoundException Resource not found exception.
     * @throws InvalidInputException Invalid input exception.
     * @throws AbsentTagException Absent tag exception.
     * @throws EntityNotFoundException Entity not found exception.
     */
    @Override
    public String execute(List<String> p_commandValues)
            throws InvalidMapException,
            ResourceNotFoundException,
            InvalidInputException,
            AbsentTagException,
            EntityNotFoundException {
        try {
            EditMapService l_editMapService = new EditMapService();
            
            String resolvedPathToFile = FindFilePathUtil.resolveFilePath(p_commandValues.get(0));
            String response = l_editMapService.handleLoadMap(resolvedPathToFile, false);

            try {
                
                ValidateMapAdapter l_validateObj = new ValidateMapAdapter();
                l_validateObj.execute(null, "loadmap");
            } catch (InvalidMapException | EntityNotFoundException l_e) {
                UserCoreLogic.getGameEngine().initialise();
                throw l_e;
            }
            
            d_logEntryBuffer.dataChanged("loadmap", response);
            return response;
        } catch (ArrayIndexOutOfBoundsException p_e) {
            throw new InvalidInputException("File name is empty!");
        }
    }
}

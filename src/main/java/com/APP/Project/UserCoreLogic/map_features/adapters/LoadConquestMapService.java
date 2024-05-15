package com.APP.Project.UserCoreLogic.map_features.adapters;

import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.constants.interfaces.StandaloneCommand;
import com.APP.Project.UserCoreLogic.exceptions.*;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;
import com.APP.Project.UserCoreLogic.Utility.FindFilePathUtil;

import java.util.List;

/**
 * LoadConquestMapService class is used to load the conquest map.
 * It implements StandaloneCommand interface.
 * It loads the conquest map and validates it.
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class LoadConquestMapService implements StandaloneCommand {

    private LogEntryBuffer d_logEntryBuffer = LogEntryBuffer.getLogger();

     /**
      * This method is used to load the conquest map.
      * @param p_commandValues List of command values.
      * @return Response of the command.
      * @throws InvalidMapException Invalid map exception.
      * @throws ResourceNotFoundException Resource not found exception.
      * @throws InvalidInputException Invalid input exception.
      * @throws AbsentTagException Absent tag exception.
      * @throws EntityNotFoundException Entity not found exception.
      */
    @Override
    public String execute(List<String> p_commandValues) throws InvalidMapException,
            ResourceNotFoundException,
            InvalidInputException,
            AbsentTagException,
            EntityNotFoundException {
        try {
            EditConquestMapService l_editConquestMapService = new EditConquestMapService();
            
            String l_resolvedPathToFile = FindFilePathUtil.resolveFilePath(p_commandValues.get(0));
            String l_response = l_editConquestMapService.loadConquestMap(l_resolvedPathToFile, false);
            try {
                ValidateConquestMapAdapter l_validateObj = new ValidateConquestMapAdapter();
                l_validateObj.execute(null);
            } catch (InvalidMapException | EntityNotFoundException l_e) {
                UserCoreLogic.getGameEngine().getMapEditorEngine().initialise();
                throw l_e;
            }
            // Logging
            d_logEntryBuffer.dataChanged("loadmap", l_response);
            return l_response;
        } catch (ArrayIndexOutOfBoundsException p_e) {
            throw new InvalidInputException("File name is empty!");
        }
    }
}

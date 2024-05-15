package com.APP.Project.UserCoreLogic.map_features.adapters;

import com.APP.Project.UserCoreLogic.exceptions.UserCoreLogicException;

import java.util.List;

/**
 * SaveMapAdapter class is used to save the map.
 * It extends SaveMapService class.
 * It saves the map and validates it.
 * It is an adapter class for SaveConquestMapService.
 * 
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class SaveMapAdapter extends SaveMapService {

    private final SaveConquestMapService d_saveConquestMapService;

    /**
     * Constructor of SaveMapAdapter.
     * 
     * @param p_saveConquestMapService SaveConquestMapService object.
     */
    public SaveMapAdapter(SaveConquestMapService p_saveConquestMapService) {
        d_saveConquestMapService = p_saveConquestMapService;
    }

    /**
     * Executes the saving of the map by delegating the task to SaveConquestMapService.
     * 
     * @param p_arguments List of arguments.
     * @return String indicating the result of the execution.
     * @throws UserCoreLogicException If an error occurs during execution.
     */
    @Override
    public String execute(List<String> p_arguments) throws UserCoreLogicException {
        return d_saveConquestMapService.execute(p_arguments);
    }
}

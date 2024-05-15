package com.APP.Project.UserCoreLogic.map_features.adapters;

import com.APP.Project.UserCoreLogic.exceptions.UserCoreLogicException;

import java.util.List;

/**
 * This class represents an adapter for editing maps.
 *
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class EditMapAdapter extends EditMapService {

    private final EditConquestMapService d_editConquestMapService;

    /**
     * Constructs an EditMapAdapter with the provided EditConquestMapService.
     *
     * @param p_editConquestMapService The EditConquestMapService to use.
     */
    public EditMapAdapter(EditConquestMapService p_editConquestMapService) {
        d_editConquestMapService = p_editConquestMapService;
    }

    /**
     * Executes the editing operation based on the provided command values.
     *
     * @param p_commandValues The list of command values.
     * @return A string representing the result of the execution.
     * @throws UserCoreLogicException if an error occurs during execution.
     */
    @Override
    public String execute(List<String> p_commandValues) throws UserCoreLogicException {
        return d_editConquestMapService.execute(p_commandValues);
    }
}

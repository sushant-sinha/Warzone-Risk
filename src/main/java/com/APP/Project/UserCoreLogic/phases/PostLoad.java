package com.APP.Project.UserCoreLogic.phases;

import com.APP.Project.UserCoreLogic.GameEngine;
import com.APP.Project.UserCoreLogic.exceptions.InvalidCommandException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidInputException;
import com.APP.Project.UserCoreLogic.exceptions.UserCoreLogicException;
import com.APP.Project.UserCoreLogic.map_features.adapters.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Represents the phase after loading the map.
 * This phase allows editing and validation of the loaded map.
 *
 * @author Raj Kumar Ramesh
 * @version 1.0
 */
public class PostLoad extends MapEditor {
    /**
     * Constructs a new PostLoad phase with the specified game engine.
     *
     * @param p_gameEngine the game engine
     */
    public PostLoad(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    /**
     * Edits the map with the provided arguments. This method is overridden 
     * from the superclass and returns an invalid command message.
     *
     * @param p_arguments the list of arguments for editing the map
     * @return an invalid command message
     * @throws UserCoreLogicException if an error occurs during the operation
     */
    @Override
    public String prepareTournament(List<Map<String, List<String>>> p_arguments) throws UserCoreLogicException {
        return this.invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String editMap(List<String> p_arguments) throws UserCoreLogicException {
        return invalidCommand();
    }

    /**
     * Edits the continent with the specified service type and arguments.
     *
     * @param l_serviceType the type of service for editing continent
     * @param p_arguments   the list of arguments for editing the continent
     * @return the result of invoking the method for continent editing
     * @throws UserCoreLogicException if an error occurs during the operation
     */
    @Override
    public String editContinent(String l_serviceType, List<String> p_arguments) throws UserCoreLogicException {
        return this.invokeMethod(new ContinentAdapter(), l_serviceType, p_arguments);
    }

    /**
     * Edits the country with the specified service type and arguments.
     *
     * @param l_serviceType the type of service for editing country
     * @param p_arguments   the list of arguments for editing the country
     * @return the result of invoking the method for country editing
     * @throws UserCoreLogicException if an error occurs during the operation
     */
    @Override
    public String editCountry(String l_serviceType, List<String> p_arguments) throws UserCoreLogicException {
        return this.invokeMethod(new CountryAdapter(), l_serviceType, p_arguments);
    }

    /**
     * Edits the neighbor with the specified service type and arguments.
     *
     * @param l_serviceType the type of service for editing neighbor
     * @param p_arguments   the list of arguments for editing the neighbor
     * @return the result of invoking the method for neighbor editing
     * @throws UserCoreLogicException if an error occurs during the operation
     */
    @Override
    public String editNeighbor(String l_serviceType, List<String> p_arguments) throws UserCoreLogicException {
        return this.invokeMethod(new CountryNeighborAdapter(), l_serviceType, p_arguments);
    }

    /**
     * Validates the map with the provided arguments.
     *
     * @param p_arguments the list of arguments for validating the map
     * @return the result of map validation
     * @throws UserCoreLogicException if an error occurs during the operation
     */
    @Override
    public String validateMap(List<String> p_arguments) throws UserCoreLogicException {
        ValidateMapAdapter l_validateMapService = new ValidateMapAdapter();
        return l_validateMapService.execute(p_arguments);
    }

    /**
     * Saves the map with the provided arguments.
     *
     * @param p_arguments the list of arguments for saving the map
     * @return the result of map saving
     * @throws UserCoreLogicException if an error occurs during the operation
     */
    @Override
    public String saveMap(List<String> p_arguments) throws UserCoreLogicException {
        SaveMapService l_saveMapService;
        if (!p_arguments.isEmpty()) {
            if (p_arguments.get(1).equalsIgnoreCase("warzone")) {
                l_saveMapService = new SaveMapService();
            } else if (p_arguments.get(1).equalsIgnoreCase("conquest")) {
                l_saveMapService = new SaveMapAdapter(new SaveConquestMapService());
            } else {
                throw new InvalidCommandException("Map type is not valid");
            }
        } else {
            throw new InvalidInputException("Empty arguments found. Please provide required arguments.");
        }
        return l_saveMapService.execute(p_arguments);
    }

    /**
     * Throws a UserCoreLogicException indicating that the map must be loaded 
     * to proceed to the next state.
     *
     * @throws UserCoreLogicException if the map is not loaded
     */
    @Override
    public void nextState() throws UserCoreLogicException {
        throw new UserCoreLogicException("Map must be loaded!");
    }
}

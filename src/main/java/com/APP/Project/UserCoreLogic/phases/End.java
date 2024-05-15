package com.APP.Project.UserCoreLogic.phases;

import com.APP.Project.UserCoreLogic.GameEngine;
import com.APP.Project.UserCoreLogic.exceptions.UserCoreLogicException;

import java.util.List;
import java.util.Map;

/**
 * Implements the method available for this phase of game.
 *
 * @author Raj Kumar Ramesh
 * @version 1.0
 */
public class End extends Phase {
    /**
     * Parameterised constructor to create an instance of <code>End</code>.
     *
     * @param p_gameEngine Instance of the game engine.
     */
    End(GameEngine p_gameEngine) {
        super(p_gameEngine);
        System.out.println("Exiting Game");
        System.exit(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String prepareTournament(List<Map<String, List<String>>> p_arguments) throws UserCoreLogicException {
        return this.invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String loadMap(List<String> p_arguments) throws UserCoreLogicException {
        return invalidCommand();
    }
    // Similar JavaDoc entries for the other overridden methods...
    /**
     * {@inheritDoc}
     */
    @Override
    public String editMap(List<String> p_arguments) throws UserCoreLogicException {
        return invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String showMap(List<String> p_arguments) throws UserCoreLogicException {
        return invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String editContinent(String l_serviceType, List<String> p_arguments) throws UserCoreLogicException {
        return this.invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String editCountry(String l_serviceType, List<String> p_arguments) throws UserCoreLogicException {
        return this.invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String editNeighbor(String l_serviceType, List<String> p_arguments) throws UserCoreLogicException {
        return this.invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String saveMap(List<String> p_arguments) throws UserCoreLogicException {
        return invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String validateMap(List<String> p_arguments) throws UserCoreLogicException {
        return invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String setPlayers(String p_serviceType, List<String> p_arguments) throws UserCoreLogicException {
        return invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String assignCountries(List<String> p_arguments) throws UserCoreLogicException {
        return invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reinforce() throws UserCoreLogicException {
        invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void issueOrder() throws UserCoreLogicException {
        invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fortify() throws UserCoreLogicException {
        invalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endGame(List<String> p_arguments) throws UserCoreLogicException {
        invalidCommand();
    }

    /**
     * {@inheritDoc}
     * In the End phase, attempting to transition to the next state is considered
     * invalid.
     */
    @Override
    public void nextState() throws UserCoreLogicException {
        invalidCommand();
    }
}

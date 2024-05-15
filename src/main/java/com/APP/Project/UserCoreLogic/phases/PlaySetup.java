package com.APP.Project.UserCoreLogic.phases;

import com.APP.Project.UserCoreLogic.GameEngine;
import com.APP.Project.UserCoreLogic.exceptions.UserCoreLogicException;
import com.APP.Project.UserCoreLogic.gamePlay.services.CountryDistributionService;
import com.APP.Project.UserCoreLogic.gamePlay.services.PlayerService;

import java.util.List;

/**
 * Concrete state of <code>Play</code>. This class is being used to add player(s) to the game and let players assign
 * countries.
 *
 * @author Raj Kumar Ramesh
 * @version 1.0
 */
public class PlaySetup extends GamePlay {
    /**
     * Constructor for PlaySetup class.
     *
     * @param p_gameEngine The GameEngine object for this phase.
     */
    public PlaySetup(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    /**
     * Loads the map for the game.
     *
     * @param p_arguments The arguments required for loading the map.
     * @return A message indicating the map has been loaded.
     * @throws UserCoreLogicException If an error occurs while loading the map.
     */
    public String loadMap(List<String> p_arguments) throws UserCoreLogicException {
        throw new UserCoreLogicException("map has been loaded");
    }

    /**
     * Sets up players for the game.
     *
     * @param serviceType The type of service for managing game players.
     * @param p_arguments The arguments required for setting up players.
     * @return A message indicating the success of setting up players.
     * @throws UserCoreLogicException If an error occurs while setting up players.
     */
    public String setPlayers(String serviceType, List<String> p_arguments) throws UserCoreLogicException {
        return this.invokeMethod(new PlayerService(), serviceType, p_arguments);
    }

    /**
     * Assigns countries to players.
     *
     * @param p_arguments The arguments required for assigning countries.
     * @return A message indicating the success of assigning countries.
     * @throws UserCoreLogicException If an error occurs while assigning countries.
     */
    public String assignCountries(List<String> p_arguments) throws UserCoreLogicException {
        CountryDistributionService l_distributeCountriesService = new CountryDistributionService();
        String l_responseValue = l_distributeCountriesService.execute(p_arguments);
        // Start game loop.
        this.d_gameEngine.getGamePlayEngine().startGameLoop();
        return l_responseValue;
    }

    /**
     * Placeholder method for reinforcement phase.
     *
     * @throws UserCoreLogicException If an invalid command is issued.
     */
    public void reinforce() throws UserCoreLogicException {
        invalidCommand();
    }

    /**
     * Placeholder method for issuing orders.
     *
     * @throws UserCoreLogicException If an invalid command is issued.
     */
    public void issueOrder() throws UserCoreLogicException {
        invalidCommand();
    }

    /**
     * Placeholder method for fortification phase.
     *
     * @throws UserCoreLogicException If an invalid command is issued.
     */
    public void fortify() throws UserCoreLogicException {
        invalidCommand();
    }

    /**
     * Placeholder method for ending the game.
     *
     * @param p_arguments The arguments required for ending the game.
     * @throws UserCoreLogicException If an invalid command is issued.
     */
    public void endGame(List<String> p_arguments) throws UserCoreLogicException {
        d_gameEngine.setGamePhase(new End(d_gameEngine));
    }

    /**
     * Transition to the next game phase.
     */
    public void nextState() {
        d_gameEngine.setGamePhase(new Reinforcement(d_gameEngine));
    }
}
package com.APP.Project.UserCoreLogic.phases;

import com.APP.Project.UserCoreLogic.GameEngine;
import com.APP.Project.UserCoreLogic.exceptions.UserCoreLogicException;

import java.util.List;
import java.util.Map;

/**
 * Serves as an abstract base for phases within the main gameplay, overseeing
 * commands specific to active gameplay states.
 * <p>
 * Within the context of the game's state pattern, this class represents a
 * higher-level categorization for phases that
 * occur during the main portion of gameplay. It extends the {@link GamePlay}
 * class, inheriting its basic structure while
 * providing specialized implementations that declare certain commands invalid
 * that are not applicable during the main play.
 * This ensures that only appropriate actions are available to players,
 * maintaining the game's integrity and flow.
 * </p>
 *
 * <p>
 * Subclasses of MainPlay are expected to implement or override methods to cater
 * to the specific requirements and allowed
 * commands of each individual gameplay phase, such as reinforcement, issue
 * orders, and execute orders phases.
 * </p>
 * 
 * @author Raj Kumar Ramesh
 * @version 1.0
 */
public abstract class MainPlay extends GamePlay {
    /**
     * Constructs a new instance of MainPlay, initializing it with the provided game
     * engine.
     *
     * @param p_gameEngine The game engine instance managing the overall state and
     *                     logic of the game.
     */
    MainPlay(GameEngine p_gameEngine) {
        super(p_gameEngine);
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
     * Declares the loading of maps as an invalid command within the main gameplay
     * phases.
     */
    @Override
    public String loadMap(List<String> p_arguments) throws UserCoreLogicException {
        return this.invalidCommand();
    }

    /**
     * {@inheritDoc}
     * Declares setting players as an invalid command once the game has entered the
     * main play phases.
     */
    @Override
    public String setPlayers(String serviceType, List<String> p_arguments) throws UserCoreLogicException {
        return this.invalidCommand();
    }

    /**
     * {@inheritDoc}
     * Declares assigning countries to players as an invalid operation during the
     * main phases of gameplay.
     */
    @Override
    public String assignCountries(List<String> p_arguments) throws UserCoreLogicException {
        return this.invalidCommand();
    }
}

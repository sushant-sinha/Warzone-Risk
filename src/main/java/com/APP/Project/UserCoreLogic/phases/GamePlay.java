package com.APP.Project.UserCoreLogic.phases;

import com.APP.Project.UserCoreLogic.GameEngine;
import com.APP.Project.UserCoreLogic.gamePlay.services.DisplayMapService;
import com.APP.Project.UserCoreLogic.exceptions.UserCoreLogicException;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * This class and its subclasses represent specific states within the gameplay
 * part of a game managed by a
 * {@link GameEngine}. It defines common behaviors for commands that are valid
 * across various gameplay states,
 * such as showing the map, and provides a default response for commands that
 * are not valid within these states.
 * Subclasses should override methods to provide state-specific behaviors where
 * necessary.
 * </p>
 *
 * @author Raj Kumar Ramesh
 * @version 1.0
 */
public abstract class GamePlay extends Phase {
    /**
     * Parameterised constructor to create an instance of <code>Play</code>.
     *
     * @param p_gameEngine Instance of the game engine.
     */
    GamePlay(GameEngine p_gameEngine) {
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
     * Displays the current state of the game map.
     * <p>
     * This command is generally valid across various states within the gameplay
     * phase, allowing players
     * to view the current configuration of the game world.
     * </p>
     *
     * @param p_arguments Arguments provided with the command, if any.
     * @return A string representation of the game map.
     * @throws UserCoreLogicException If an error occurs during map display.
     */
    @Override
    public String showMap(List<String> p_arguments) throws UserCoreLogicException {
        DisplayMapService l_showMapService = new DisplayMapService();
        return l_showMapService.execute(p_arguments);
    }

    /**
     * Attempts to edit the game map, which is not allowed during the gameplay
     * phase.
     * <p>
     * This method overrides the corresponding method in the {@link Phase} class to
     * reflect the specific restrictions
     * of the gameplay phase. Editing the map is a task typically reserved for the
     * setup or post-game phases, not during
     * active gameplay. Thus, invoking this command during the gameplay phase will
     * result in an error message being returned,
     * indicating that the command is invalid in the current state.
     * </p>
     *
     * @param p_arguments A list of arguments provided for the edit map command.
     *                    These are ignored in the gameplay phase
     *                    since the command is considered invalid.
     * @return A string message indicating that the edit map command is not valid
     *         during the gameplay phase.
     * @throws UserCoreLogicException If any issue arises while processing this
     *                                command, encapsulating different types
     *                                of exceptions that could occur.
     */
    @Override
    public String editMap(List<String> p_arguments) throws UserCoreLogicException {
        return this.invalidCommand();
    }

    /**
        * Attempts to save the current map configuration to a file, which is not
     * permitted in the game play phase.
     * <p>
     * Invoking this command during the game play phase is considered invalid since
     * map modifications are typically
     * restricted to the setup or post-game phases. This method ensures that the
     * game's integrity is maintained by
     * preventing unauthorized or unintended map alterations during critical
     * gameplay periods.
     * </p>
     *
     * @param p_arguments The arguments provided with the save map command, not used
     *                    here as the command is invalid.
     * @return A string message indicating that the save map operation is invalid in
     *         the current game phase.
     * @throws UserCoreLogicException if the operation is attempted, encapsulating
     *                                the reason why the command is not allowed.
     */
    @Override
    public String saveMap(List<String> p_arguments) throws UserCoreLogicException {
        return this.invalidCommand();
    }

    /**
     * Attempts to edit continent information during the gameplay phase, which is
     * not permitted.
     * <p>
     * This method override signifies that continent editing is considered an
     * invalid operation during the gameplay phase
     * of the game. Editing continents is typically restricted to the pre-game setup
     * phase to ensure game integrity and
     * fairness among players. Attempting to invoke this command during gameplay
     * will result in a standardized message
     * indicating the invalidity of the action.
     * </p>
     *
     * @param l_serviceType The type of service being requested for the continent
     *                      edit, not used here as the operation is invalid.
     * @param p_arguments   The arguments provided with the edit continent command,
     *                      not used here as the command is invalid.
     * @return A string message indicating that editing continent information is
     *         invalid in the current game phase.
     * @throws UserCoreLogicException if the operation is attempted, encapsulating
     *                                the reason why the command is not allowed.
     */
    @Override
    public String editContinent(String l_serviceType, List<String> p_arguments) throws UserCoreLogicException {
        return this.invalidCommand();
    }

    /**
     * Attempts to edit country details, which is not allowed during the gameplay
     * phase.
     * <p>
     * This method override signifies that country modifications are not permissible
     * once the game has entered
     * its gameplay phase. Such operations are typically restricted to the initial
     * setup phase or specific
     * editing phases to ensure the game's integrity and state consistency.
     * Attempting to invoke this command
     * during gameplay will result in an error message indicating that the operation
     * is invalid.
     * </p>
     *
     * @param l_serviceType The type of editing service requested, not used here as
     *                      the command is considered invalid.
     * @param p_arguments   A list of arguments provided for editing the country,
     *                      not used here due to the operation's invalidity.
     * @return A string message indicating that editing a country is not allowed in
     *         the current game phase.
     * @throws UserCoreLogicException Signifies that the operation is not permitted,
     *                                encapsulating the reason.
     */
    @Override
    public String editCountry(String l_serviceType, List<String> p_arguments) throws UserCoreLogicException {
        return this.invalidCommand();
    }

    /**
     * Attempts to edit the neighboring relationships between countries, an action
     * that is not permissible during the gameplay phase.
     * <p>
     * Editing neighboring relationships between countries is critical during the
     * map setup and preparation phases but is restricted
     * once the game transitions into active gameplay. This ensures the stability
     * and fairness of the game environment by preventing
     * mid-game alterations to the geographical or strategic landscape. Invoking
     * this operation during the gameplay phase will result
     * in a standardized error response, indicating the invalidity of such actions
     * at this stage.
     * </p>
     *
     * @param l_serviceType The type of neighbor editing operation requested, which
     *                      is not applicable as the command is deemed invalid
     *                      during gameplay.
     * @param p_arguments   The list of arguments intended for the neighbor editing
     *                      operation, not utilized due to the command's
     *                      inadmissibility in this phase.
     * @return A string message conveying that neighbor editing is not an allowable
     *         action within the current game phase.
     * @throws UserCoreLogicException Indicates that the attempted operation is
     *                                forbidden, encapsulating the underlying reason
     *                                for its inadmissibility.
     */
    @Override
    public String editNeighbor(String l_serviceType, List<String> p_arguments) throws UserCoreLogicException {
        return this.invalidCommand();
    }

    /**
     * Attempts to validate the current map configuration, an operation not
     * permitted during the gameplay phase.
     * <p>
     * Map validation is typically performed during the map editing or initial setup
     * phases to ensure the map's
     * suitability for gameplay. Once the game enters the gameplay phase, the map is
     * considered final and immutable
     * to maintain fairness and integrity. Invoking this command during gameplay
     * will result in an error message,
     * indicating that map validation is not an applicable operation at this stage
     * of the game.
     * </p>
     *
     * @param p_arguments A list of arguments provided with the validate map
     *                    command, not utilized here as the operation is deemed
     *                    invalid.
     * @return A string message indicating that map validation is not allowed in the
     *         current game phase.
     * @throws UserCoreLogicException Signifies that the operation is not permitted,
     *                                encapsulating the reason for its invalidity.
     */
    @Override
    public String validateMap(List<String> p_arguments) throws UserCoreLogicException {
        return this.invalidCommand();
    }

    /**
     * Transitions the game to the end phase, concluding the current game session.
     * <p>
     * This method is called when the game reaches a conclusion, either through
     * player action or other game
     * conditions being met. It transitions the game to the {@link End} phase, where
     * no further gameplay actions
     * are permitted.
     * </p>
     *
     * @param p_arguments Arguments provided with the command, if any.
     * @throws UserCoreLogicException If an error occurs while transitioning to the
     *                                end phase.
     */
    @Override
    public void endGame(List<String> p_arguments) throws UserCoreLogicException {
        d_gameEngine.setGamePhase(new End(d_gameEngine));
    }
}

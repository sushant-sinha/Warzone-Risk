package com.APP.Project.UserCoreLogic.phases;

import com.APP.Project.UserCoreLogic.exceptions.InvalidInputException;
import com.APP.Project.UserCoreLogic.exceptions.UserCoreLogicException;
import com.APP.Project.UserCoreLogic.gamePlay.GamePlayEngine;
import com.APP.Project.UserCoreLogic.gamePlay.services.ExecuteOrderService;
import com.APP.Project.UserCoreLogic.exceptions.ResourceNotFoundException;

/**
 * Represents the phase in the game where all issued orders are executed.
 * <p>
 * This phase is responsible for carrying out the actions determined by players'
 * orders. It is a critical phase in the
 * game's lifecycle, transitioning the game state based on strategic decisions
 * made by the players. The execution of
 * orders can include movements of armies, battles, and other strategic actions
 * that affect the game board. After all
 * orders are executed, the game transitions to the next phase, typically
 * reinforcement.
 * </p>
 *
 * @author Raj Kumar Ramesh
 * @version 1.0
 */
public class Execute extends MainPlay {
    /**
     * Parameterised constructor to create an instance of <code>Fortify</code>.
     *
     * @param p_gameEngine Instance of the game engine.
     */
    Execute(com.APP.Project.UserCoreLogic.GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    /**
     * {@inheritDoc}
     * In the Execute phase, reinforcing is not allowed, and invoking this command
     * will result in an error.
     */
    @Override
    public void reinforce() throws UserCoreLogicException {
        invalidCommand();
    }

    /**
     * {@inheritDoc}
     * In the Execute phase, issuing new orders is not allowed, and invoking this
     * command will result in an error.
     */
    @Override
    public void issueOrder() throws UserCoreLogicException {
        invalidCommand();
    }

    /**
     * {@inheritDoc}
     * Executes all pending orders issued by players.
     * <p>
     * This method utilizes the {@link ExecuteOrderService} to process and execute
     * all orders. It encapsulates
     * the logic for the execution of game actions dictated by player orders.
     * Exceptions may be thrown if issues
     * arise during order execution.
     * </p>
     *
     * @throws ResourceNotFoundException If required resources for executing an
     *                                   order are not found.
     * @throws InvalidInputException     If an order contains invalid inputs.
     */
    @Override
    public void fortify() throws ResourceNotFoundException, InvalidInputException {
        ExecuteOrderService l_executeOrderService = new ExecuteOrderService();
        l_executeOrderService.execute();
    }

    /**
     * {@inheritDoc}
     * Transitions the game to the next phase, typically reinforcement, after all
     * orders have been executed.
     * <p>
     * This method updates the game's phase state and prepares the game for the next
     * set of player actions.
     * </p>
     */
    @Override
    public void nextState() {
        GamePlayEngine.incrementEngineIndex();
        d_gameEngine.setGamePhase(new Reinforcement(d_gameEngine));
    }
}

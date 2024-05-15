package com.APP.Project.UserCoreLogic.phases;

import com.APP.Project.UserCoreLogic.GameEngine;
import com.APP.Project.UserCoreLogic.gamePlay.services.IssueOrderService;
import com.APP.Project.UserCoreLogic.exceptions.InvalidInputException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidOrderException;
import com.APP.Project.UserCoreLogic.exceptions.ResourceNotFoundException;
import com.APP.Project.UserCoreLogic.exceptions.UserCoreLogicException;

/**
 * The phase in the game where players are prompted to issue their orders, such
 * as moving armies or executing special actions.
 * <p>
 * During the IssueOrder phase, players formulate and submit their strategies
 * for the current turn. This phase is critical
 * for the dynamic progression of the game, allowing for strategic depth and
 * player agency. Only the issuing of orders is
 * permitted during this phase, with restrictions placed on actions that do not
 * pertain to order formulation, such as reinforcement
 * or fortification, to maintain the phase's focus and integrity.
 * </p>
 *
 * @author Raj Kumar Ramesh
 * @version 1.0
 */
public class IssueOrder extends MainPlay {
    /**
     * Constructs a new IssueOrder phase with the specified game engine context.
     *
     * @param p_gameEngine The game engine instance managing the overall state and
     *                     logic of the game.
     */
    public IssueOrder(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    /**
     * {@inheritDoc}
     * Reinforcement is not allowed in the IssueOrder phase and invoking this method
     * will signal an invalid operation.
     */
    @Override
    public void reinforce() throws UserCoreLogicException {
        invalidCommand();
    }

    /**
     * Prompts the IssueOrderService to execute, allowing players to submit their
     * orders for the turn.
     * <p>
     * This method is the core of the IssueOrder phase, enabling the capture and
     * processing of player orders
     * through the IssueOrderService. It represents the transition from planning to
     * action within the game's turn cycle.
     * </p>
     *
     * @throws ResourceNotFoundException If necessary resources for order issuance
     *                                   are not found.
     * @throws InvalidInputException     If inputs for the orders are invalid.
     */
    @Override
    public void issueOrder() throws ResourceNotFoundException, InvalidInputException, InvalidOrderException {
        IssueOrderService l_issueOrderService = new IssueOrderService();
        l_issueOrderService.execute();
    }

    /**
     * {@inheritDoc}
     * Fortification is not allowed in the IssueOrder phase, invoking this method
     * will signal an invalid operation.
     */
    @Override
    public void fortify() throws UserCoreLogicException {
        invalidCommand();
    }

    /**
     * Transitions the game to the next phase, typically moving to order execution
     * after all orders have been issued.
     */
    public void nextState() {
        d_gameEngine.setGamePhase(new Execute(d_gameEngine));
    }
}

package com.APP.Project.UserCoreLogic.phases;

import com.APP.Project.UserCoreLogic.GameEngine;
import com.APP.Project.UserCoreLogic.gamePlay.services.ReinforcementService;
import com.APP.Project.UserCoreLogic.exceptions.UserCoreLogicException;

/**
  * This class represents the reinforcement phase of the game. 
 * During this phase, players reinforce their territories.
 *
 * @author Raj Kumar Ramesh
 * @version 1.0
 */
public class Reinforcement extends MainPlay {
    /**
     * Constructor for Reinforcement class.
     * 
     * @param p_gameEngine The game engine instance.
     */
    public Reinforcement(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    /**
   * Executes the reinforcement phase by calling the ReinforcementService.
     * 
     * @throws UserCoreLogicException If an error occurs during the reinforcement phase.
     */
    @Override
    public void reinforce() throws UserCoreLogicException {
        ReinforcementService l_reinforcementService = new ReinforcementService();
        l_reinforcementService.execute();
    }

    /**
     * Method stub for issueOrder phase which is not applicable in the reinforcement phase.
     * 
     * @throws UserCoreLogicException If an invalid command is issued.
     */
    @Override
    public void issueOrder() throws UserCoreLogicException {
        invalidCommand();
    }

    /**
     * Method stub for fortify phase which is not applicable in the reinforcement phase.
     * 
     * @throws UserCoreLogicException If an invalid command is issued.
     */
    @Override
    public void fortify() throws UserCoreLogicException {
        invalidCommand();
    }

    /**
     * Transition to the next game phase which is IssueOrder.
     */
    @Override
    public void nextState() {
        d_gameEngine.setGamePhase(new IssueOrder(d_gameEngine));
    }
}

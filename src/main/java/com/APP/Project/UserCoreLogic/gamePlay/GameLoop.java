package com.APP.Project.UserCoreLogic.gamePlay;

import com.APP.Project.UserCoreLogic.GameEngine;
import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.exceptions.GameLoopIllegalStateException;
import com.APP.Project.UserCoreLogic.exceptions.UserCoreLogicException;
import com.APP.Project.UserCoreLogic.phases.Execute;
import com.APP.Project.UserCoreLogic.phases.IssueOrder;
import com.APP.Project.UserCoreLogic.phases.PlaySetup;
import com.APP.Project.UserCoreLogic.phases.Reinforcement;

/**
 * This class is responsible for managing the  players and their orders with runtime information.
 * It is additionally responsible for executing orders in a round-robin manner.
 *
 * @author Rupal Kapoor
 * @version 1.0
 */
public class GameLoop {
    private GamePlayEngine d_gamePlayEngine;
    public volatile boolean d_isAlive = false;

    public GameLoop(GamePlayEngine p_gamePlayEngine) {
        d_gamePlayEngine = p_gamePlayEngine;
    }

    public void run() {
        if (d_isAlive) {
            return;
        }
        d_isAlive = true;
        com.APP.Project.UserCoreLogic.GameEngine l_gameEngine = UserCoreLogic.getGameEngine();
        try {
            if (l_gameEngine.getGamePhase().getClass().equals(PlaySetup.class)) {
                l_gameEngine.getGamePhase().nextState();
            } else if (l_gameEngine.getGamePhase().getClass().equals(IssueOrder.class)) {
                // When the game is loaded and it was in IssueOrder when saved.
            } else {
                throw new GameLoopIllegalStateException("Illegal state transition!");
            }
            // Responsive to thread interruption.
            while (d_isAlive) {
                if (l_gameEngine.getGamePhase().getClass().equals(Reinforcement.class)) {
                    l_gameEngine.getGamePhase().reinforce();
                }
                if (l_gameEngine.getGamePhase().getClass().equals(IssueOrder.class)) {
                    l_gameEngine.getGamePhase().issueOrder();
                }
                if (l_gameEngine.getGamePhase().getClass().equals(Execute.class)) {
                    l_gameEngine.getGamePhase().fortify();
                    if (d_gamePlayEngine.checkIfGameIsOver()) {
                        // If the game is over, break the main-game-loop.
                        break;
                    }
                }
                l_gameEngine.getGamePhase().nextState();
            }
        } catch (UserCoreLogicException p_vmException) {
            UserCoreLogic.getInstance().stderr(p_vmException.getMessage());
        } finally {
            // This will set CLI#UserInteractionState to WAIT
            UserCoreLogic.getInstance().stdout("GAME_ENGINE_STOPPED");
        }
    }

    public void stop() {
        d_isAlive = false;
    }

    public boolean isAlive() {
        return d_isAlive;
    }
}

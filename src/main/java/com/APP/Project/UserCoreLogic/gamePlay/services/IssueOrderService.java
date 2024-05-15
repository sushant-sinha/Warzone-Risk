package com.APP.Project.UserCoreLogic.gamePlay.services;

import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.exceptions.InvalidArgumentException;
import com.APP.Project.UserCoreLogic.game_entities.Player;
import com.APP.Project.UserCoreLogic.exceptions.EntityNotFoundException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidCommandException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidOrderException;
import com.APP.Project.UserCoreLogic.gamePlay.GamePlayEngine;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * The service responsible for requesting players for issuing orders.
 *
 * @author Rupal Kapoor
 * @version 1.0
 */
public class IssueOrderService {
    private final LogEntryBuffer d_logEntryBuffer = LogEntryBuffer.getLogger();

    /**
     * This method is used to request all players in round-robin fashion for the issuing order until all the players have placed all their
     * reinforcement armies on the map.
     * In case the player issues an order with reinforcements more than enough they possess, it will request the same player
     * again for a valid order.
     */
    public void execute() throws InvalidOrderException {
        List<Player> finishedIssuingOrders = new ArrayList<>();
        GamePlayEngine l_gamePlayEngine = UserCoreLogic.getGameEngine().getGamePlayEngine();
        l_gamePlayEngine.setCurrentPlayerTurn(l_gamePlayEngine.getCurrentPlayerForIssuePhase());

        while (finishedIssuingOrders.size() != l_gamePlayEngine.getPlayerList().size()) {
            // Find player who has reinforcements.
            Player l_currentPlayer;
            do {
                l_currentPlayer = l_gamePlayEngine.getCurrentPlayer();
            } while (finishedIssuingOrders.contains(l_currentPlayer));

            // Until player issues the valid order.
            boolean l_invalidPreviousOrder;
            do {
                try {
                    // Request player to issue the order.
                    l_currentPlayer.issueOrder();
                    if (l_currentPlayer.isDone()) {
                        // Player won't be asked again for issuing orders for this phase.
                        finishedIssuingOrders.add(l_currentPlayer);
                    }
                    l_invalidPreviousOrder = false;
                } catch (EntityNotFoundException | InvalidCommandException | InvalidArgumentException p_exception) {
                    l_invalidPreviousOrder = true;
                    // Show VMException error to the user.
                    UserCoreLogic.getInstance().stderr(p_exception.getMessage());

                    // Logging
                    d_logEntryBuffer.dataChanged("issue_order_error", p_exception.getMessage());
                } catch (InterruptedException | ExecutionException p_e) {
                    // If interruption occurred while issuing the order.
                    l_invalidPreviousOrder = true;
                }
            } while (l_invalidPreviousOrder);
        }

        // Store to use when starting the issue phase again.
        l_gamePlayEngine.setCurrentPlayerForIssuePhase(l_gamePlayEngine.getCurrentPlayerTurn());
    }
}
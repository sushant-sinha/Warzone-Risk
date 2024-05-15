package com.APP.Project.UserCoreLogic.gamePlay.services;

import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.constants.interfaces.Order;
import com.APP.Project.UserCoreLogic.gamePlay.GamePlayEngine;
import com.APP.Project.UserCoreLogic.game_entities.Player;
import com.APP.Project.UserCoreLogic.exceptions.CardNotFoundException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidOrderException;
import com.APP.Project.UserCoreLogic.exceptions.OrderOutOfBoundException;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * The service is used to execute the player's order.
 *
 * @author Rupal Kapoor
 * @version 1.0
 */
public class ExecuteOrderService {
    private final LogEntryBuffer d_logEntryBuffer = LogEntryBuffer.getLogger();

    /**
     * This method gets the order of the player using Player class's next order method and executes it using the type of order.
     */
    public void execute() {
        List<Player> finishedExecutingOrders = new ArrayList<>();

        UserCoreLogic.getInstance().stdout("Execution of orders started!");
        d_logEntryBuffer.dataChanged("execution_order", "Execution of orders started!");

        GamePlayEngine l_gamePlayEngine = UserCoreLogic.getGameEngine().getGamePlayEngine();
        l_gamePlayEngine.setCurrentPlayerTurn(l_gamePlayEngine.getCurrentPlayerForExecutionPhase());

        // Iterate over and execute the orders which were supposed to be executed in this phase.
        UserCoreLogic.getGameEngine().getGamePlayEngine().getCurrentFutureOrders().forEach(l_futureOrder -> {
            try {
                l_futureOrder.execute();
                UserCoreLogic.getInstance().stdout(String.format("Executing %s's order", l_futureOrder.getOwner().getName()));
                UserCoreLogic.getInstance().stdout(String.format("Executed %s", l_futureOrder.toString()));
            } catch (InvalidOrderException | CardNotFoundException p_e) {
                UserCoreLogic.getInstance().stderr(p_e.getMessage());
            }
        });

        // Expire orders which had been executed and are not valid anymore.
        UserCoreLogic.getGameEngine().getGamePlayEngine().getExpiredFutureOrders().forEach(l_futureOrder -> {
            l_futureOrder.expire();
            UserCoreLogic.getGameEngine().getGamePlayEngine().removeFutureOrder(l_futureOrder);
        });

        while (finishedExecutingOrders.size() != l_gamePlayEngine.getPlayerList().size()) {
            // Find player who has remaining orders to execute.
            Player l_currentPlayer;
            do {
                l_currentPlayer = l_gamePlayEngine.getCurrentPlayer();
            } while (finishedExecutingOrders.contains(l_currentPlayer));

            UserCoreLogic.getInstance().stdout(String.format("Executing %s's order", l_currentPlayer.getName()));
            try {
                // Get the next order
                Order l_currentOrder = l_currentPlayer.nextOrder();
                // If order supposed to be executed in the next phase.
                if (l_currentOrder.getExecutionIndex() == GamePlayEngine.getCurrentExecutionIndex()) {
                    l_currentOrder.execute();
                    UserCoreLogic.getInstance().stdout(String.format("\nExecuted %s", l_currentOrder.toString()));
                }

                // If the current player does not have any orders left.
                if (!l_currentPlayer.hasOrders()) {
                    finishedExecutingOrders.add(l_currentPlayer);
                }
            } catch (CardNotFoundException |
                     InvalidOrderException p_e) {
                // Logging
                d_logEntryBuffer.dataChanged("execute_order_error", String.format("%s: %s", l_currentPlayer.getName(), p_e.getMessage()));
                UserCoreLogic.getInstance().stderr(p_e.getMessage());
            } catch (OrderOutOfBoundException p_e) {
                d_logEntryBuffer.dataChanged("execute_order_warning", p_e.getMessage());
                finishedExecutingOrders.add(l_currentPlayer);
            }
        }

        // Store to use when starting the issue phase again.
        l_gamePlayEngine.setCurrentPlayerForExecutionPhase(l_gamePlayEngine.getCurrentPlayerTurn());
    }
}

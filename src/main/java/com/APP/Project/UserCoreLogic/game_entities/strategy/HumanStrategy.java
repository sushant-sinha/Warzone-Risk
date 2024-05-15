package com.APP.Project.UserCoreLogic.game_entities.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.constants.enums.StrategyType;
import com.APP.Project.UserCoreLogic.constants.interfaces.Order;
import com.APP.Project.UserCoreLogic.game_entities.Player;
import com.APP.Project.UserCoreLogic.exceptions.EntityNotFoundException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidArgumentException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidCommandException;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;
import com.APP.Project.UserCoreLogic.mappers.OrderMapper;
import com.APP.Project.UserCoreLogic.responses.CommandResponses;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * This class defines the behaviour of human player.
 *
 * @author Sushant Sinha
 */
public class HumanStrategy extends PlayerStrategy {
    private final LogEntryBuffer d_logEntryBuffer = LogEntryBuffer.getLogger();
    /**
     * To map from <code>UserCommand</code> to <code>Order</code>.
     */
    private final OrderMapper d_orderMapper = new OrderMapper();

    public HumanStrategy(Player p_player) {
        super(p_player);
    }

    /**
     * Gets the order from the user and stores the order for the player.
     *
     * @throws InvalidCommandException  If there is an error while preprocessing the user command.
     * @throws InvalidArgumentException If the mentioned value is not of expected type.
     * @throws EntityNotFoundException  If the target country not found.
     * @throws ExecutionException       If any error while processing concurrent thread.
     * @throws InterruptedException     If scheduled thread was interrupted.
     */
    @Override
    public void execute() throws
            InvalidCommandException,
            EntityNotFoundException,
            ExecutionException,
            InterruptedException,
            InvalidArgumentException {
        // Requests user interface for input from user.
        String l_responseVal = "";
        do {
            UserCoreLogic.getInstance().stdout(String.format("\nPlayer: %s--------\nUSAGE: You can check map details\n> showmap <return>", this.d_player.getName(), this.d_player.getRemainingReinforcementCount()));
            Future<String> l_responseOfFuture = UserCoreLogic.getInstance().askForUserInput(String.format("Issue Order:"));
            l_responseVal = l_responseOfFuture.get();
            d_logEntryBuffer.dataChanged("issue_order", String.format("%s player's turn to Issue Order", this.d_player.getName()));
        } while (l_responseVal.isEmpty());
        try {
            ObjectMapper l_objectMapper = new ObjectMapper();
            // Map user response to Order object.
            CommandResponses l_commandResponse = l_objectMapper.readValue(l_responseVal, CommandResponses.class);
            if (l_commandResponse.isDone()) {
                d_logEntryBuffer.dataChanged("issue_order", String.format("%s player's finished issuing the orders", this.d_player.getName()));
                this.d_player.doneWithOrder();
                return;
            }
            Order l_newOrder = d_orderMapper.toOrder(l_commandResponse, this.d_player);
            d_logEntryBuffer.dataChanged("issue_order", l_newOrder.toString());
            this.d_player.addOrder(l_newOrder);
        } catch (IOException p_ioException) {
            throw new InvalidCommandException("Unrecognised input!");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StrategyType getType() {
        return StrategyType.HUMAN;
    }
}

package com.APP.Project.UserCoreLogic.game_entities.strategy;

import com.APP.Project.UserCoreLogic.constants.enums.StrategyType;
import com.APP.Project.UserCoreLogic.game_entities.Player;
import com.APP.Project.UserCoreLogic.exceptions.EntityNotFoundException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidArgumentException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidCommandException;

import java.util.concurrent.ExecutionException;

/**
 * Represents the strategy of the player.
 *
 * @author Sushant Sinha
 */
public abstract class PlayerStrategy {
    protected Player d_player;

    protected PlayerStrategy(Player p_player) {
        d_player = p_player;
    }

    /**
     * Executes the strategy implemented by the concrete class.
     *
     * @throws InvalidCommandException  If there is an error while preprocessing the user command.
     * @throws InvalidArgumentException If the mentioned value is not of expected type.
     * @throws EntityNotFoundException  If the target country not found.
     * @throws ExecutionException       If any error while processing concurrent thread.
     * @throws InterruptedException     If scheduled thread was interrupted.
     */
    public abstract void execute() throws
            InvalidCommandException,
            EntityNotFoundException,
            ExecutionException,
            InterruptedException,
            InvalidArgumentException;

    /**
     * Gets the type of strategy.
     *
     * @return Type of the strategy.
     */
    public abstract StrategyType getType();
}

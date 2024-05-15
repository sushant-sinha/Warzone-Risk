package com.APP.Project.UserCoreLogic.constants.interfaces;

import com.APP.Project.UserCoreLogic.constants.enums.OrderTypes;
import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.gamePlay.GamePlayEngine;
import com.APP.Project.UserCoreLogic.game_entities.Player;
import com.APP.Project.UserCoreLogic.exceptions.CardNotFoundException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidOrderException;

/**
 * Defines the contract for different types of orders to implement, specifying methods that must be provided.
 *
 * @author Bhoomiben Bhatt
 * @version 1.0
 */
public abstract class Order implements JSONable {
    /**
     * Specifies the execution sequence in the game play where this order should be executed.
     */
    private final int d_executionIndex;
    /**
     * Indicates the expiration timing for this order.
     */
    private int d_expiryIndex = -1;
    private final Player d_owner;

    /**
     * Initializes a new instance of the class with the specified player as the issuer of the order.
     *
     * @param p_player The player issuing this order.
     */
    public Order(Player p_player) {
        d_owner = p_player;
        if (this.getType() == OrderTypes.negotiate) {
            d_executionIndex = GamePlayEngine.getCurrentExecutionIndex() + 1;
            d_expiryIndex = d_executionIndex + 1;
            UserCoreLogic.getGameEngine().getGamePlayEngine().addFutureOrder(this);
        } else {
            d_executionIndex = GamePlayEngine.getCurrentExecutionIndex();
        }
    }

    /**
     * Carries out the order in the <code>GameLoopState#EXECUTE_ORDER</code> phase of the game loop.
     *
     * @throws InvalidOrderException If the order cannot be executed due to issues like an invalid country, incorrect army count,
     *  *                               or other invalid inputs.
     * @throws CardNotFoundException If the required card is not found in the player's card list.
     */
    public abstract void execute() throws InvalidOrderException, CardNotFoundException;

    /**
     * Retrieves the order's type.
     *
     * @return The type of this order.
     */
    public abstract OrderTypes getType();

    /**
     * Retrieves the player who issued the order.
     *
     * @return The player who owns this order.
     */
    public Player getOwner() {
        return d_owner;
    }

    /**
     * Retrieves the execution sequence number for this order.
     *
     * @return The execution index of this order.
     */
    public int getExecutionIndex() {
        return d_executionIndex;
    }

    /**
     * Retrieves the index at which this order is set to expire.
     *
     * @return The expiration index of this order.
     */
    public int getExpiryIndex() {
        return d_expiryIndex;
    }

    /**
     * Reverses the order's effects or sets the associated card to expired, undoing previous execution.
     */
    abstract public void expire();
}

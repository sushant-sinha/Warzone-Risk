package com.APP.Project.UserCoreLogic.game_entities;

/**
 * This class provides the result of the game.
 *
 * @author Sushant Sinha
 * @version 1.0
 */
public class GameResult {
    /**
     * If the game results in a draw.
     */
    private final boolean d_declaredDraw;

    private final Player d_winnerPlayer;

    /**
     * If error occurrs during the game round.
     */
    private final boolean error;

    /**
     * A parameterised constructor for storing the result of the game.
     *
     * @param p_declaredDraw Value representing if the game has been a draw or not.
     * @param p_winnerPlayer Value of the winner player.
     */
    public GameResult(boolean p_declaredDraw, Player p_winnerPlayer) {
        d_declaredDraw = p_declaredDraw;
        d_winnerPlayer = p_winnerPlayer;
        error = false;
    }

    /**
     * A parameterised constructor to set the boolean to true for <code>error</code> because the game has an interruption while in the running stage.
     */
    public GameResult() {
        d_declaredDraw = false;
        d_winnerPlayer = null;
        error = true;
    }

    /**
     * Checks if the game is a draw or not.
     *
     * @return True if the game was a draw; false otherwise.
     */
    public boolean isDeclaredDraw() {
        return d_declaredDraw;
    }

    /**
     * Gets the player who won the game.
     *
     * @return Winner player.
     */
    public Player getWinnerPlayer() {
        return d_winnerPlayer;
    }

    /**
     * Checks if the game had an interruption.
     *
     * @return True if the game was interrupted; false otherwise.
     */
    public boolean isError() {
        return error;
    }
}

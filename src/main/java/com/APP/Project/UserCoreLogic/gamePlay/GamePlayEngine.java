
package com.APP.Project.UserCoreLogic.gamePlay;

import com.APP.Project.UserCoreLogic.TournamentEngine;
import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.constants.interfaces.Engine;
import com.APP.Project.UserCoreLogic.constants.interfaces.JSONable;
import com.APP.Project.UserCoreLogic.constants.interfaces.Order;
import com.APP.Project.UserCoreLogic.exceptions.InvalidGameException;
import com.APP.Project.UserCoreLogic.Container.PlayerContainer;
import com.APP.Project.UserCoreLogic.game_entities.GameResult;
import com.APP.Project.UserCoreLogic.game_entities.Player;
import com.APP.Project.UserCoreLogic.exceptions.EntityNotFoundException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Manages players and their orders runtime information; Responsible for executing orders in round-robin fashion.
 *
 * @author Rupal Kapoor
 * @version 1.0
 */
public class GamePlayEngine implements Engine, JSONable {
    /**
     * This is a list of the players of the game.
     */
    private List<Player> d_playerList;

    /**
     * Denotes the current turn of the player for issuing the order.
     */
    private int d_currentPlayerTurn = 0;

    /**
     * This attribute enables to keeps the track of the first player that was selected by the engine while the game loop issue order
     * state.
     */
    private int d_currentPlayerForIssuePhase = 0;

    /**
     * This attribute helps to keep the track of the first player selected by the engine while game loop executes the order state.
     */
    private int d_currentPlayerForExecutionPhase = 0;

    /**
     * This is the thread created by the GamePlayEngine. It is responsive to interruption.
     */
    private Thread d_LoopThread;

    /**
     * This represents the main game loop.
     */
    private GameLoop d_gameLoop = new GameLoop(this);

    /**
     * This metric keeps track of the execution-index, i.e. it helps to decide order of execution and expiration phase.
     */
    private static int d_currentExecutionIndex = 0;

    /**
     * This captures the list of the future orders which are supposed to be executed later in the iterations.
     */
    private final List<Order> d_futurePhaseOrders = new ArrayList<>();

    /**
     * This attribute captures the result of the game.
     * It is set to null until the game is over.
     */
    private GameResult d_gameResult;

    private final static PlayerContainer d_PLAYER_REPOSITORY = new PlayerContainer();

    /**
     * This is to privatize the instance creation of this class
     */
    public GamePlayEngine() {
        this.initialise();
    }

    /**
     * This sets the execution-index using offset by using the below prameterised constructor to .
     *
     * @param p_offset This represents the offset value to be set for the execution index.
     */
    public GamePlayEngine(int p_offset) {
        this.initialise();
        d_currentExecutionIndex = p_offset;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        d_playerList = new ArrayList<>();
    }

    /**
     * This method add the player to the list.
     *
     * @param p_player This denotes the player to be added.
     */
    public void addPlayer(Player p_player) {
        d_playerList.add(p_player);
    }

    /**
     * This method removes the player from the list.
     *
     * @param p_player This denotes the player to be removed.
     */
    public void removePlayer(Player p_player) {
        d_playerList.remove(p_player);
    }

    /**
     * This method gets the players of the game.
     *
     * @return the value of the list of players.
     */
    public List<Player> getPlayerList() {
        return d_playerList;
    }

    /**
     * This method is used to set the players of the game.
     *
     * @param p_playerList the value returned is the player list.
     */
    public void setPlayerList(List<Player> p_playerList) {
        d_playerList = p_playerList;
    }

    /**
     * This method gets the player who has the turn for issuing the order.
     *
     * @return Player object of the player which will issue the order.
     */
    public Player getCurrentPlayer() {
        Player l_currentPlayer = d_playerList.get(d_currentPlayerTurn);
        d_currentPlayerTurn++;
        // Round-robin fashion
        if (d_currentPlayerTurn >= d_playerList.size()) {
            d_currentPlayerTurn = 0;
        }
        return l_currentPlayer;
    }

    /**
     * This method gets the index of current player
     *
     * @return The value of index of current player
     */
    public int getCurrentPlayerTurn() {
        return d_currentPlayerTurn;
    }

    /**
     * This method sets the index of current player.
     *
     * @param p_currentPlayerTurn This is the value of index of current player.
     */
    public void setCurrentPlayerTurn(int p_currentPlayerTurn) {
        d_currentPlayerTurn = p_currentPlayerTurn;
    }

    /**
     * This method gets the previously stored player index who has the turn to issue an order.
     *
     * @return Value of the index.
     */
    public int getCurrentPlayerForIssuePhase() {
        return d_currentPlayerForIssuePhase;
    }

    /**
     * This method sets the index of the player that is going to issue an order in the next iteration.
     *
     * @param p_currentPlayerForIssuePhase The value of the index to be set
     */
    public void setCurrentPlayerForIssuePhase(int p_currentPlayerForIssuePhase) {
        d_currentPlayerForIssuePhase = p_currentPlayerForIssuePhase;
    }

    /**
     * This method gets the previously stored player index to get an order of the player for the execution
     *
     * @return The value of the index.
     */
    public int getCurrentPlayerForExecutionPhase() {
        return d_currentPlayerForExecutionPhase;
    }

    /**
     * This method sets the index of the player for which the order is going to be executed first in the next iteration.
     *
     * @param p_currentPlayerForExecutionPhase The value of the index to be set.
     */
    public void setCurrentPlayerForExecutionPhase(int p_currentPlayerForExecutionPhase) {
        d_currentPlayerForExecutionPhase = p_currentPlayerForExecutionPhase;
    }

    /**
     * This method gets the current execution index. This index helps to keep track of orders, some of which should be executed and
     * others should be expired during this loop iteration.
     *
     * @return The integer value of the index.
     */
    public static int getCurrentExecutionIndex() {
        return d_currentExecutionIndex;
    }

    /**
     * This method gets the list of future orders which should be executed during this phase.
     *
     * @return The value of the list of orders.
     */
    public List<Order> getCurrentFutureOrders() {
        return d_futurePhaseOrders.stream().filter(p_futureOrder ->
                p_futureOrder.getExecutionIndex() == d_currentExecutionIndex
        ).collect(Collectors.toList());
    }

    /**
     * This method gets the list of future orders which are going to be expired after current loop iteration.
     *
     * @return The value of the list of orders.
     */
    public List<Order> getExpiredFutureOrders() {
        return d_futurePhaseOrders.stream().filter(p_futureOrder ->
                p_futureOrder.getExpiryIndex() <= d_currentExecutionIndex
        ).collect(Collectors.toList());
    }

    /**
     * This method adds the order to be executed in future iteration.
     *
     * @param p_futureOrder The value of the order to be added.
     */
    public void addFutureOrder(Order p_futureOrder) {
        this.d_futurePhaseOrders.add(p_futureOrder);
    }

    /**
     * This method is used to remove the order. It is called if the order has been expired.
     *
     * @param p_futureOrder The value of the order to be added.
     */
    public void removeFutureOrder(Order p_futureOrder) {
        this.d_futurePhaseOrders.remove(p_futureOrder);
    }

    /**
     * This method is responsible for incrementing the current execution index.
     */
    public static void incrementEngineIndex() {
        d_currentExecutionIndex++;
    }

    /**
     * This method starts the thread to iterate through various game loop states. Channels the exception to
     * stderr method.
     */
    public void startGameLoop() {
        UserCoreLogic.getInstance().stdout("GAME_ENGINE_STARTED");
        if (d_gameLoop.isAlive()) {
            d_gameLoop.stop();
        }
        if (d_LoopThread != null && d_LoopThread.isAlive()) {
            d_LoopThread.interrupt();
        }
        d_LoopThread = new Thread(() -> {
            d_gameLoop.run();
        });
        d_LoopThread.start();
    }

    /**
     * This method is responsible for returning the  thread representing the game loop.
     *
     * @return Thread The thread representing game loop
     */
    public Thread getLoopThread() {
        return d_LoopThread;
    }

    /**
     * This method checks if the game round will be over only when any player has won the game. If the game mode is tournament, then
     * additional condition for the game to be over is to exceed the turns of the round.
     *
     * @return The value True is returned if the game is over, else false is returned.
     */
    public boolean checkIfGameIsOver() {
        if (UserCoreLogic.getGameEngine().isTournamentModeOn() && d_currentExecutionIndex > TournamentEngine.getInstance().getMaxNumberOfTurns()) {
            d_gameResult = new GameResult(true, null);
            return true;
        }
        List<Player> l_playerWhoWonTheGame =
                UserCoreLogic.getGameEngine().getGamePlayEngine().getPlayerList().stream().filter(Player::isWon).collect(Collectors.toList());
        if (l_playerWhoWonTheGame.size() > 0) {
            d_gameResult = new GameResult(false, l_playerWhoWonTheGame.get(0));
            return true;
        }
        return false;
    }

    /**
     * This method fetches the result of the game.
     *
     * @return The value of the game result to be returned.
     */
    public GameResult getGameResult() {
        return d_gameResult;
    }

    /**
     * This method is used to set the result of the game.
     *
     * @param p_gameResult The value of result of the game is returned
     */
    public void setGameResult(GameResult p_gameResult) {
        d_gameResult = p_gameResult;
    }

    /**
     * This method is responsible for shutting the game
     */
    public void shutdown() {
        if (d_gameLoop.isAlive()) {
            d_gameLoop.stop();
        }
        // Interrupt thread if it is alive.
        if (d_LoopThread != null && d_LoopThread.isAlive())
            d_LoopThread.interrupt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJSON() {
        JSONObject l_gamePlayEngineJSON = new JSONObject();
        JSONArray l_playerJSONList = new JSONArray();
        JSONObject l_friendPlayerJSON = new JSONObject();
        for (Player l_player : getPlayerList()) {
            l_playerJSONList.put(l_player.toJSON());
            JSONArray l_friendPlayers = new JSONArray();
            for (Player l_friendPlayer : l_player.getFriendPlayers()) {
                l_friendPlayers.put(l_friendPlayer.getName());
            }
            l_friendPlayerJSON.put(l_player.getName(), l_friendPlayers);
        }
        l_gamePlayEngineJSON.put("players", l_playerJSONList);
        l_gamePlayEngineJSON.put("friendPlayerMappings", l_friendPlayerJSON);
        l_gamePlayEngineJSON.put("currentPlayerForIssuePhase", getCurrentPlayerForIssuePhase());
        l_gamePlayEngineJSON.put("currentPlayerForExecutionPhase", getCurrentPlayerForExecutionPhase());
        l_gamePlayEngineJSON.put("currentExecutionIndex", getCurrentExecutionIndex());
        return l_gamePlayEngineJSON;
    }

    /**
     * This method is used to assign the data members of the concrete class using the values inside the JSONObject
     *
     * @param p_jsonObject This represents the JSONObject holding runtime information.
     * @param p_gameEngine This is the instance of target GamePlayEngine.
     * @return This returns the created instance of this class using the JSON data that is provided.
     * @throws InvalidGameException It is throwns in case the information from JSONObject cannot be used due to being corrupted or missing
     *                              the values.
     */
    public static GamePlayEngine fromJSON(JSONObject p_jsonObject, com.APP.Project.UserCoreLogic.GameEngine p_gameEngine) throws
            InvalidGameException {
        GamePlayEngine l_gamePlayEngine = new GamePlayEngine(p_jsonObject.getInt("currentExecutionIndex"));
        p_gameEngine.setGamePlayEngine(l_gamePlayEngine);

        JSONArray l_playerJSONList = p_jsonObject.getJSONArray("players");
        for (int l_playerIndex = 0; l_playerIndex < l_playerJSONList.length(); l_playerIndex++) {
            Player l_player = Player.fromJSON(l_playerJSONList.getJSONObject(l_playerIndex));
            l_gamePlayEngine.addPlayer(l_player);
        }

        JSONObject l_friendPlayerJSON = p_jsonObject.getJSONObject("friendPlayerMappings");
        Set<String> l_friendPlayerNameSet = l_friendPlayerJSON.keySet();
        try {
            for (String l_playerName : l_friendPlayerNameSet) {
                Player l_player = d_PLAYER_REPOSITORY.findByPlayerName(l_playerName);
                JSONArray l_friendPlayerNames = l_friendPlayerJSON.getJSONArray(l_playerName);
                for (int l_friendPlayerIndex = 0; l_friendPlayerIndex < l_friendPlayerNames.length(); l_friendPlayerIndex++) {
                    String l_friendPlayerName = l_friendPlayerNames.getString(l_friendPlayerIndex);
                    Player l_friendPlayer = d_PLAYER_REPOSITORY.findByPlayerName(l_friendPlayerName);
                    l_player.addNegotiatePlayer(l_friendPlayer);
                }
            }
        } catch (EntityNotFoundException p_entityNotFoundException) {
            throw new InvalidGameException();
        }

        l_gamePlayEngine.setCurrentPlayerForIssuePhase(p_jsonObject.getInt("currentPlayerForIssuePhase"));
        l_gamePlayEngine.setCurrentPlayerForExecutionPhase(p_jsonObject.getInt("currentPlayerForExecutionPhase"));
        return l_gamePlayEngine;
    }
}

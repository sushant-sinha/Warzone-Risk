package com.APP.Project.UserCoreLogic;

import com.APP.Project.InterfaceCoreMiddleware;
import com.APP.Project.UserCoreLogic.exceptions.ResourceNotFoundException;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;
import com.APP.Project.UserCoreLogic.logger.LogWriter;
import com.APP.Project.UserCoreLogic.phases.Phase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * this redirects the user to different interaction state and stores the information of the game state and other game related features.
 *
 * @author Jayati Thakkar
 * @version 1.0
 */
public class UserCoreLogic {
    private static UserCoreLogic d_Instance;

    private static GameEngine d_gameEngine;

    private InterfaceCoreMiddleware d_userInterfaceMiddleware;

    private final ExecutorService d_executor = Executors.newFixedThreadPool(10);

    private LogEntryBuffer d_logEntryBuffer;
    private LogWriter d_logWriter;

    /**
     * It creates an instance of <code>UserCoreLogic</code> class.
     * @return the instance of the same class
     */
    public static UserCoreLogic newInstance() {
        d_Instance = new UserCoreLogic();
        d_gameEngine = new GameEngine();
        d_Instance.d_logEntryBuffer = LogEntryBuffer.getLogger();
        try {
            d_Instance.d_logWriter = new LogWriter(d_Instance.d_logEntryBuffer);
        } catch (ResourceNotFoundException p_e) {
            UserCoreLogic.getInstance().stderr("LogEntryBuffer failed!");
        }
        return d_Instance;
    }

    /**
     * it initialises all the engines to store run time information
     */
    public void initialise() {
        UserCoreLogic.getGameEngine().initialise();
        UserCoreLogic.TOURNAMENT_ENGINE().initialise();
    }

    /**
     * it exists the entire engine.
     */
    public static void exit() {
        getGameEngine().shutdown();
        TOURNAMENT_ENGINE().shutdown();
        UserCoreLogic.getInstance().stdout("Shutting down...");
    }

    /**
     * it attaches the middleware for ther user interaction and the core logic.
     * @param p_userInterfaceMiddleware
     */
    public void attachUIMiddleware(InterfaceCoreMiddleware p_userInterfaceMiddleware) {
        d_userInterfaceMiddleware = p_userInterfaceMiddleware;
    }

    /**
     * it returns an instance of <code>UserCoreLogic</code> class we created earlier.
     * @return the instance
     * @throws NullPointerException
     */
    public static UserCoreLogic getInstance() throws NullPointerException {
        if (d_Instance == null) {
            throw new NullPointerException("Virtual Machine was not created. Something went wrong.");
        }
        return d_Instance;
    }

    /**
     * Sets the game engine to store runtime information of the game.
     *
     * @param p_gameEngine Value of the game engine.
     */
    public static void setGameEngine(GameEngine p_gameEngine) {
        d_gameEngine = p_gameEngine;
    }

    /**
     * Gets game engine to store runtime information of the game.
     *
     * @return Value of the game engine.
     */
    public static GameEngine getGameEngine() {
        return d_gameEngine;
    }

    /**
     * Gets tournament engine to store information of the game while the game mode is tournament.
     *
     * @return Value of the game engine.
     */
    public static TournamentEngine TOURNAMENT_ENGINE() {
        return TournamentEngine.getInstance();
    }

    /**
     * Gets the state of the game
     *
     * @return Value of the game state
     */
    public static Phase getGamePhase() {
        return d_gameEngine.getGamePhase();
    }

    public Future<String> askForUserInput(String p_message) {
        return d_executor.submit(() ->
                d_userInterfaceMiddleware.askForUserInput(p_message)
        );
    }

    /**
     * Sends the message to output channel of the user interface.
     *
     * @param p_message Represents the message.
     */
    public void stdout(String p_message) {
        if (d_userInterfaceMiddleware != null)
            d_userInterfaceMiddleware.stdout(p_message);
    }

    /**
     * Sends the message to error channel of the user interface.
     *
     * @param p_message Represents the error message.
     */
    public void stderr(String p_message) {
        if (d_userInterfaceMiddleware != null)
            d_userInterfaceMiddleware.stderr(p_message);
    }
}



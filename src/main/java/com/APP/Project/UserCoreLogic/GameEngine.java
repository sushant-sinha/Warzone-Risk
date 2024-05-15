package com.APP.Project.UserCoreLogic;

import com.APP.Project.UserCoreLogic.constants.interfaces.JSONable;
import com.APP.Project.UserCoreLogic.exceptions.InvalidGameException;
import com.APP.Project.UserCoreLogic.exceptions.ResourceNotFoundException;
import com.APP.Project.UserCoreLogic.gamePlay.GamePlayEngine;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;
import com.APP.Project.UserCoreLogic.logger.LogWriter;
import com.APP.Project.UserCoreLogic.map_features.MapEditorEngine;
import com.APP.Project.UserCoreLogic.phases.*;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This Game Engine creates an environment for the entire game for the user to play.
 *
 * @author Jayati Thakkar
 * @version 1.0
 */
public class GameEngine  implements JSONable {
    /**
     * An instance of the same class
     */
    private GamePlayEngine d_gamePlayEngine;
    /**
     * <code>MapEditorEngine</code> for this game.
     * VM runtime map-editor engine to store map runtime information.
     */
    private MapEditorEngine d_mapEditorEngine;

    /**
     * Phase object of the GameEngine state
     */
    private Phase d_gameState;

    private boolean d_isTournamentModeOn = false;

    /**
     * Gets the instance of the <code>GameEngine</code> class.
     *
     */
    public GameEngine() {
        this.initialise();
        // MAP_EDITOR ENGINE
        d_mapEditorEngine = new MapEditorEngine();
        d_mapEditorEngine.initialise();
        // GAME_PLAY ENGINE
        d_gamePlayEngine = new GamePlayEngine();
        d_gamePlayEngine.initialise();
    }

    /**
     * Tournament round is set by preparing the gameplayengine and mapeditorengines.
     * @param p_mapEditorEngine map editor engine
     * @param p_gamePlayEngine game play engine
     */
    public GameEngine(MapEditorEngine p_mapEditorEngine, GamePlayEngine p_gamePlayEngine) {
        this.initialise();
        d_mapEditorEngine = p_mapEditorEngine;
        d_gamePlayEngine = p_gamePlayEngine;
        d_isTournamentModeOn = true;
    }

    /**
     * It initialises the engines to reset the runtime information.
     */
    public void initialise() {
        this.setGamePhase(new Preload(this));
    }

    /**
     * SShutsdown all the engines.
     */
    public void shutdown() {
        d_mapEditorEngine.shutdown();
        d_gamePlayEngine.shutdown();
    }

    /**
     * Sets new phase for the game.
     *
     * @param p_gamePhase New value of the game phase.
     */
    public void setGamePhase(Phase p_gamePhase) {
        d_gameState = p_gamePhase;
    }

    /**
     * Returns the phase of game.
     *
     * @return Value of the game phase.
     */
    public Phase getGamePhase() {
        return d_gameState;
    }

    /**
     * Returns UserCoreLogic map-editor engine to store map information at runtime.
     *
     * @return Value of the map editor engine.
     */
    public MapEditorEngine getMapEditorEngine() {
        return d_mapEditorEngine;
    }


    /**
     * ets map-editor engine having the runtime information.
     *
     * @param p_mapEditorEngine The value to be set
     */
    public void setMapEditorEngine(MapEditorEngine p_mapEditorEngine) {
        d_mapEditorEngine = p_mapEditorEngine;
    }

    /**
     * Gets VM runtime game-play engine to store runtime after game starts.
     *
     * @return Value of the game-play engine.
     */
    public GamePlayEngine getGamePlayEngine() {
        return d_gamePlayEngine;
    }

    /**
     * Sets game-play engine having the runtime information.
     *
     * @param p_gamePlayEngine Value of the game-play engine.
     */
    public void setGamePlayEngine(GamePlayEngine p_gamePlayEngine) {
        d_gamePlayEngine = p_gamePlayEngine;
    }

    /**
     * Check if the tournament mode is on.
     *
     * @return True if the game mode is tournament; false otherwise.
     */
    public boolean isTournamentModeOn() {
        return d_isTournamentModeOn;
    }

    /**
     * Set the tournament mode
     *
     * @param p_tournamentMode True if the tournament mode is on; false otherwise.
     */
    public void setTournamentMode(boolean p_tournamentMode) {
        d_isTournamentModeOn = p_tournamentMode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJSON() {
        JSONObject l_gameEngineJSON = new JSONObject();
        l_gameEngineJSON.put("map_editor", this.getMapEditorEngine().toJSON());
        l_gameEngineJSON.put("game_pay", this.getGamePlayEngine().toJSON());
        l_gameEngineJSON.put("phase", this.getGamePhase().getClass().getSimpleName());
        return l_gameEngineJSON;
    }

    public static GameEngine fromJSON(JSONObject p_jsonObject) throws InvalidGameException {
        try {
            // Create and load GameEngine.
            GameEngine l_gameEngine = new GameEngine();
            UserCoreLogic.setGameEngine(l_gameEngine);

            MapEditorEngine.fromJSON(p_jsonObject.getJSONObject("map_editor"), l_gameEngine);
            GamePlayEngine l_gamePlayEngine = GamePlayEngine.fromJSON(p_jsonObject.getJSONObject("game_pay"), l_gameEngine);

            // Set the game phase.
            // The phase can be only from the following.
            String l_phaseString = p_jsonObject.getString("phase");
            if (l_phaseString.equals(Preload.class.getSimpleName())) {
                l_gameEngine.setGamePhase(new Preload(l_gameEngine));
            } else if (l_phaseString.equals(PostLoad.class.getSimpleName())) {
                l_gameEngine.setGamePhase(new PostLoad(l_gameEngine));
            } else if (l_phaseString.equals(PlaySetup.class.getSimpleName())) {
                l_gameEngine.setGamePhase(new PlaySetup(l_gameEngine));
            } else if (l_phaseString.equals(IssueOrder.class.getSimpleName())) {
                l_gameEngine.setGamePhase(new IssueOrder(l_gameEngine));
                l_gamePlayEngine.startGameLoop();
            }
            return l_gameEngine;
        } catch (JSONException p_jsonException) {
            throw new InvalidGameException("Missing values or the corrupted game file!");
        }
    }
}
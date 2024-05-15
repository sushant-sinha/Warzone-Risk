package com.APP.Project.UserCoreLogic.gamePlay.services;

import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.constants.enums.StrategyType;
import com.APP.Project.UserCoreLogic.Container.PlayerContainer;
import com.APP.Project.UserCoreLogic.game_entities.Player;
import com.APP.Project.UserCoreLogic.exceptions.EntityNotFoundException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidInputException;
import com.APP.Project.UserCoreLogic.gamePlay.GamePlayEngine;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;

/**
 * This class deals with managing the gameplayer user command to add and/or remove game player from the game
 *
 * @author Rupal Kapoor
 * @version 1.0
 */
public class PlayerService {

    /**
     * The game play engine to store and retrieve map data.
     */
    private final GamePlayEngine d_gamePlayEngine;

    /**
     * This denotes the player container.
     */
    private final PlayerContainer d_playerRepository;

    private final LogEntryBuffer d_logEntryBuffer;

    /**
     * Default constructor to instantiate objects.
     */
    public PlayerService() {
        d_gamePlayEngine = UserCoreLogic.getGameEngine().getGamePlayEngine();
        d_playerRepository = new PlayerContainer();
        d_logEntryBuffer = LogEntryBuffer.getLogger();
    }

    /**
     * This method is used to add the player to the list stored at Game Play engine.
     *
     * @param p_playerName the value of the player's name.
     * @param p_strategyType the value of strategy of the player
     * @return the value of response of the request.
     * @throws InvalidInputException is thrown in case of processing the player creation.
     */
    public String add(String p_playerName, String p_strategyType) throws InvalidInputException {
        if (!d_playerRepository.existByPlayerName(p_playerName)) {
            try {
                StrategyType l_strategyType;
                try {
                    l_strategyType = StrategyType.valueOf(p_strategyType.toUpperCase());
                } catch (IllegalArgumentException p_e) {
                    throw new InvalidInputException("Invalid strategy type!");
                }
                Player l_player = new Player(p_playerName, l_strategyType);
                d_gamePlayEngine.addPlayer(l_player);
                // Logging
                d_logEntryBuffer.dataChanged("gameplayer", p_playerName + " player added with strategy type: "+p_strategyType);

                return String.format("%s player added!", p_playerName);
            } catch (Exception e) {
                throw new InvalidInputException("Invalid player arguments!");
            }
        } else {
            throw new InvalidInputException("Player name already exists....Please provide different name.");
        }
    }

    /**
     * This method is responsible for removing the player from the list using the name.
     *
     * @param p_playerName The continent name.
     * @return The value of response of the request.
     * @throws EntityNotFoundException is thrown in case the player with the provided name is not found.
     */
    public String remove(String p_playerName) throws EntityNotFoundException {
        // We can check if the continent exists before filtering?
        // Filters the continent list using the continent name
        Player l_player = d_playerRepository.findByPlayerName(p_playerName);
        d_gamePlayEngine.removePlayer(l_player);
        // Logging
        d_logEntryBuffer.dataChanged("gameplayer", p_playerName + " player removed!");

        return String.format("%s player removed!", p_playerName);
    }
}
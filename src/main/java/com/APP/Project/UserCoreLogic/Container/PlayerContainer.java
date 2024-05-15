package com.APP.Project.UserCoreLogic.Container;

import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.game_entities.Player;
import com.APP.Project.UserCoreLogic.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class finds the <code>Player</code> entity from the runtime engine.
 *
 * @author Bhoomiben Bhatt
 * @version 1.0
 */
public class PlayerContainer {
    /**
     * Retrieves a specific <code>Player</code> based on their name.
     *
     * @param p_playerName The name of the player to search for.
     * @return The first <code>Player</code> object that matches the specified name.
     * @throws EntityNotFoundException Throws If no player with the given name can be found.
     */
    public Player findByPlayerName(String p_playerName) throws EntityNotFoundException {
        List<Player> l_filteredPlayerList = UserCoreLogic.getGameEngine().getGamePlayEngine().getPlayerList().stream().filter(p_player ->
                p_player.getName().equals(p_playerName)
        ).collect(Collectors.toList());
        if (l_filteredPlayerList.size() > 0)
            return l_filteredPlayerList.get(0);
        throw new EntityNotFoundException(String.format("'%s' player not found", p_playerName));
    }

    /**
     * Determines if a player with the specified name already exists in the list of joined players.
     *
     * @param p_playerName The name of the player to check for uniqueness.
     * @return True if a player with the same name exists; otherwise, false.
     */
    public boolean existByPlayerName(String p_playerName) {
        return UserCoreLogic.getGameEngine().getGamePlayEngine().getPlayerList().stream().filter(p_player ->
                p_player.getName().equals(p_playerName)
        ).count() == 1;
    }
}

package com.APP.Project.UserCoreLogic.gamePlay.services;

import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.constants.enums.CardType;
import com.APP.Project.UserCoreLogic.constants.interfaces.StandaloneCommand;
import com.APP.Project.UserCoreLogic.gamePlay.GamePlayEngine;
import com.APP.Project.UserCoreLogic.map_features.adapters.ShowMapAdapter;
import com.jakewharton.fliptables.FlipTable;
import com.APP.Project.UserCoreLogic.game_entities.Country;
import com.APP.Project.UserCoreLogic.game_entities.Player;
import com.APP.Project.UserCoreLogic.exceptions.EntityNotFoundException;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for showing all countries and continents, armies on each country,
 * ownership, and connectivity in a way that enables efficient game play.
 *
 * @author Rupal Kapoor
 * @version 1.0
 */
public class DisplayMapService implements StandaloneCommand {
    GamePlayEngine d_gamePlayEngine;
    List<Player> d_playerList;
    ShowMapAdapter d_showMapService;
    private final LogEntryBuffer d_logEntryBuffer;

    /**
     * Default constructor to initializes various objects.
     *
     * @throws EntityNotFoundException is thrown if the entity not found.
     */
    public DisplayMapService() throws EntityNotFoundException {
        d_gamePlayEngine = UserCoreLogic.getGameEngine().getGamePlayEngine();
        d_playerList = d_gamePlayEngine.getPlayerList();
        d_showMapService = new ShowMapAdapter();
        d_logEntryBuffer = LogEntryBuffer.getLogger();
    }

    /**
     * This method denotes the information of countries owned by player with the army count on each country.
     *
     * @param p_player the player for whom the information will be displayed
     * @return the string with required player information
     */
    public String showPlayerContent(Player p_player) {
        List<Country> l_countryList = p_player.getAssignedCountries();
        LinkedList<String> l_countryNames = new LinkedList<>();

        //list of country names
        for (Country l_country : l_countryList) {
            l_countryNames.add(l_country.getCountryName());
        }

        //table header
        String[] l_header = new String[l_countryList.size() + 1];
        l_header[0] = p_player.getName().toUpperCase();
        for (int l_row = 1; l_row < l_header.length; l_row++) {
            l_header[l_row] = l_countryNames.pollFirst();
        }
        String[] l_playerMap = new String[l_header.length];
        l_playerMap[0] = "Army Count";
        LinkedList<Country> l_countryNames2 = new LinkedList<>();
        l_countryNames2.addAll(l_countryList);

        //showing army count per country
        for (int l_row = 1; l_row < l_playerMap.length; l_row++) {
            Country l_country = l_countryNames2.pollFirst();
            l_playerMap[l_row] = String.valueOf(l_country.getNumberOfArmies());
        }
        return FlipTable.of(l_header, new String[][]{l_playerMap});

    }

    /**
     * This method is used to initiate all the methods of ShowMapAdapter file
     *
     * @param p_commandValues the value of parameters entered by the user
     * @return the value of string of continent and neighbour country information
     * @throws EntityNotFoundException is thrown in case no player is available
     */
    @Override
    public String execute(List<String> p_commandValues) throws EntityNotFoundException {
        StringBuilder l_playerContent = new StringBuilder();
        String l_logResponse = "";
        int l_playerCount = 0;
        if (!this.d_playerList.isEmpty()) {
            for (Player l_player : d_playerList) {
                l_playerContent.append("Player " + (++l_playerCount) + "\n");
                l_playerContent.append("Total Deployed Army (previously): " + (l_player.getReinforcementCount() - l_player.getRemainingReinforcementCount()) + "\n");
                l_playerContent.append("Total Reinforcement Count: " + l_player.getReinforcementCount() + "\n");
                if (l_player.getCards().size() <= 0) {
                    l_playerContent.append("Player doesn't have any card yet.\n");
                } else {
                    // Show player's card with the total number of it.
                    Map<CardType, Integer> l_cardTypeIntegerMap = l_player.getMapOfCardTypeAndNumber();
                    for (CardType l_cardType : l_cardTypeIntegerMap.keySet()) {
                        l_playerContent.append(String.format("%s card: %d\n", l_cardType.getJsonValue(), l_cardTypeIntegerMap.get(l_cardType)));
                    }
                }
                l_playerContent.append(this.showPlayerContent(l_player));
            }

            // Logging
            l_logResponse = l_playerContent.toString() + "\n" + "CONNECTIVITY" + "\n" + d_showMapService.showNeighbourCountries();
            d_logEntryBuffer.dataChanged("showmap", l_logResponse);
            return l_logResponse;
        } else {
            throw new EntityNotFoundException("Please, add players to show game status!");
        }
    }
}

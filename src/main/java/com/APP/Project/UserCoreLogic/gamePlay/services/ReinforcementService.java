package com.APP.Project.UserCoreLogic.gamePlay.services;

import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.gamePlay.GamePlayEngine;
import com.APP.Project.UserCoreLogic.map_features.MapEditorEngine;
import com.APP.Project.UserCoreLogic.game_entities.Continent;
import com.APP.Project.UserCoreLogic.game_entities.Country;
import com.APP.Project.UserCoreLogic.game_entities.Player;
import com.APP.Project.UserCoreLogic.exceptions.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is used to reinforce the army to respective players at each new turn.
 *
 * @author Rupal Kapoor
 */
public class ReinforcementService {
    /**
     * This is the singleton instance of the MapFeatureEngine class.
     */
    public MapEditorEngine d_mapEditorEngine;

    /**
     * This is the singleton instance of GamePlayEngine.
     */
    public GamePlayEngine d_gamePlayEngine;

    /**
     * This map captures the continent and its member countries.
     */
    public Map<String, List<String>> d_continentCountryList;

    /**
     * This constructor is used to set reinforcement army to each player. It also checks whether a player completely owns a
     * continent or not. In case he/she does, then it will add the continent's control value to the reinforcement army as a part of
     * bonus.
     */
    public ReinforcementService() {
        d_mapEditorEngine = UserCoreLogic.getGameEngine().getMapEditorEngine();
        d_gamePlayEngine = UserCoreLogic.getGameEngine().getGamePlayEngine();
    }

    /**
     * Calculates the exact amount of army to be reinforced.
     *
     * @param p_player This is the player's object.
     * @param p_continentValue This is the control value that has been added if a player owns a whole continent.
     * @return This method returns the army to be reinforced to the player.
     */
    private int addReinforcementArmy(Player p_player, int p_continentValue) {
        int l_AssignedCountryCount = p_player.getAssignedCountries().size();
        int l_reinforcementArmy = Math.max(3, (int) Math.ceil(l_AssignedCountryCount / 3));

        l_reinforcementArmy = l_reinforcementArmy + p_continentValue;
        return l_reinforcementArmy;
    }

    /**
     * Checks whether a player owns a whole continent or not. In case the player owns, then control value of
     * respective continent is returned otherwise zero will be returned.
     *
     * @param p_playerList  This is the Player's object.
     * @param p_countryList The list of countries beloning to a  specific continent.
     * @param p_continent   The continent whose country is selected.
     * @return This method returns the continent's Control value if player owns whole continent otherwise return zero.
     */
    private int checkPlayerOwnsContinent(Player p_playerList, List<String> p_countryList, Continent p_continent) {
        List<String> l_country = new ArrayList<>();
        for (Country l_country1 : p_playerList.getAssignedCountries()) {
            l_country.add(l_country1.getCountryName());
        }
        boolean l_checkCountry = l_country.containsAll(p_countryList);
        if (l_checkCountry) {
            return p_continent.getContinentControlValue();
        }
        return 0;
    }

    /**
     * This method assigns each player the correct number of reinforcement armies according to the Warzone rules.
     *
     * @throws EntityNotFoundException is thrown in case the  player is not available.
     */
    public void execute() throws EntityNotFoundException {
        d_continentCountryList = d_mapEditorEngine.getContinentCountryMap();

        for (Player l_player : d_gamePlayEngine.getPlayerList()) {
            int l_continentValue = 0;
            for (Continent l_continent : d_mapEditorEngine.getContinentList()) {

                List<String> l_countryList = new ArrayList<>(d_continentCountryList.get(l_continent.getContinentName()));
                // Method Call: Here Control Value is assessed.
                int l_returnContinentValue = checkPlayerOwnsContinent(l_player, l_countryList, l_continent);

                l_continentValue = l_continentValue + l_returnContinentValue;
            }
            // Method Call: This will add reinforcement Army to the player at each turn.
            int l_returnReinforcementArmy = addReinforcementArmy(l_player, l_continentValue);
            l_player.setReinforcementCount(l_returnReinforcementArmy);
        }
    }
}
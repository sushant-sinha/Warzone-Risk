package com.APP.Project.UserCoreLogic.gamePlay.services;

import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.constants.interfaces.StandaloneCommand;
import com.APP.Project.UserCoreLogic.gamePlay.GamePlayEngine;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;
import com.APP.Project.UserCoreLogic.Container.CountryContainer;
import com.APP.Project.UserCoreLogic.game_entities.Country;
import com.APP.Project.UserCoreLogic.game_entities.Player;
import com.APP.Project.UserCoreLogic.exceptions.EntityNotFoundException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidInputException;
import com.APP.Project.UserCoreLogic.exceptions.UserCoreLogicException;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.floor;

/**
 * This service manages distribution of countries among all the players.
 *
 * @author Rupal Kapoor
 */
public class CountryDistributionService implements StandaloneCommand {
    private List<Country> d_countryList;
    /**
     * This is the country repository to lookup the countries using the filters.
     */
    private CountryContainer d_countryRepository = new CountryContainer();

    private final GamePlayEngine d_gamePlayEngine;

    private final LogEntryBuffer d_logEntryBuffer;

    /**
     * Constructor for instantiating required objects.
     */
    public CountryDistributionService() {
        d_countryList = UserCoreLogic.getGameEngine().getMapEditorEngine().getCountryList();
        d_gamePlayEngine = UserCoreLogic.getGameEngine().getGamePlayEngine();
        d_logEntryBuffer = LogEntryBuffer.getLogger();
    }

    /**
     * This method is used to assign countries to different players.
     *
     * @return the value of response as per the request.
     * @throws InvalidInputException Throws if number of players are zero.
     */
    public String distributeCountries() throws InvalidInputException {
        int l_countryCount = d_countryList.size();
        int l_playerCount = d_gamePlayEngine.getPlayerList().size();
        try {
            int l_floorVal = (int) floor(l_countryCount / l_playerCount);
            int l_remainder = l_countryCount % l_playerCount;

            for (Player l_playerObj : d_gamePlayEngine.getPlayerList()) {
                if (l_remainder > 0) {
                    l_playerObj.setAssignedCountryCount(l_floorVal + 1);
                    l_remainder--;
                } else {
                    l_playerObj.setAssignedCountryCount(l_floorVal);
                }
            }
            for (Player l_player : d_gamePlayEngine.getPlayerList()) {
                int l_playerCountryCount = l_player.getAssignedCountryCount();
                List<Country> l_assignedCountryList = assignCountry(l_player, l_playerCountryCount);
                l_player.setAssignedCountries(l_assignedCountryList);
            }
            return "Countries are successfully assigned!";
        } catch (ArithmeticException e) {
            throw new InvalidInputException("Number of players are zero");
        }
    }

    /**
     * This method gives the list of countries to be assigned to player.
     *
     * @param p_player  The object of Player class.
     * @param p_playerCountryCount The no. of countries that can be assigned to player.
     * @return The list of countries in response
     */
    public List<Country> assignCountry(Player p_player, int p_playerCountryCount) {
        List<Country> l_assignedCountries = new ArrayList<>();
        List<Country> l_countryLst;
        List<Country> l_groupOfCountries;
        int l_playerCountryCount = p_playerCountryCount;

        int l_size;
        int l_iterateCountryCount = 0;
        do {
            Country selectedCountry = d_countryList.get(l_iterateCountryCount);
            if (selectedCountry.getOwnedBy() == null) {
                selectedCountry.setOwnedBy(p_player);
                l_groupOfCountries = d_countryRepository.findCountryNeighborsAndNotOwned(selectedCountry);
                l_groupOfCountries.add(0, selectedCountry);

                l_size = l_groupOfCountries.size();
                if (l_size < p_playerCountryCount) {
                    p_playerCountryCount -= l_size;
                    assignOwnerToCountry(p_player, l_groupOfCountries);
                    l_assignedCountries.addAll(l_groupOfCountries);
                } else {
                    l_countryLst = l_groupOfCountries.subList(0, p_playerCountryCount);
                    assignOwnerToCountry(p_player, l_countryLst);
                    l_assignedCountries.addAll(l_countryLst);
                }
            }
            l_iterateCountryCount++;
            if (l_iterateCountryCount >= d_countryList.size()) {
                break;
            }
        } while (l_assignedCountries.size() < l_playerCountryCount);
        return l_assignedCountries;
    }

    /**
     * This method assigns an owner to different countries.
     *
     * @param p_player  The object of Player class.
     * @param p_countryList The list of countries in response
     */
    public void assignOwnerToCountry(Player p_player, List<Country> p_countryList) {
        for (Country l_con : p_countryList) {
            l_con.setOwnedBy(p_player);
        }
    }

    /**
     * This method internally calls the distributeCountries() method of the class and returns the result.
     *
     * @param p_commandValues Denotes the values passed while running the command.
     * @return An exception is thrown in case or error, else a success message if function runs without error
     * @throws InvalidInputException is thrown if number of players are zero.
     * @throws IllegalStateException is thrown if returns an empty list.
     * @throws UserCoreLogicException is thrown in case any exception from while players in the game loop code
     */
    @Override
    public String execute(List<String> p_commandValues) throws UserCoreLogicException, IllegalStateException {
        // Check if players have been added.
        // What if only one player is available?
        if (!d_gamePlayEngine.getPlayerList().isEmpty()) {
            String l_response = distributeCountries();
            // Logging
            d_logEntryBuffer.dataChanged("assigncountries", l_response + "\n" + this.getPlayerCountries());
            return l_response;
        } else {
            throw new EntityNotFoundException("Please, add players to show game status!");
        }
    }

    /**
     * This method returns the string of countries associated with each player.
     *
     * @return The string of all of player's countries
     */
    public String getPlayerCountries() {
        String l_playerContent = "";
        for (Player l_player : d_gamePlayEngine.getPlayerList()) {
            List<Country> l_countries = l_player.getAssignedCountries();
            List<String> l_names = new ArrayList<>();
            for (Country l_country : l_countries) {
                l_names.add(l_country.getCountryName());
            }
            String l_countriesNames = String.join(",", l_names);
            l_playerContent += l_player.getName() + ": " + l_names + "\n";
        }
        return l_playerContent;
    }
}

package com.APP.Project.UserCoreLogic.Container;

import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.game_entities.Country;
import com.APP.Project.UserCoreLogic.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class searches the Country entity from the runtime engine.
 *
 * @author Bhoomiben Bhatt
 * @version 1.0
 */
public class CountryContainer {
    /**
     * Searches for countries based on their names. This method retrieves a list of countries that match the provided name,
     * accommodating instances where multiple countries may share similar or identical names or when partial matches are considered.
     *
     * @param p_countryName The name of the country to search for.
     * @return A list of countries that match the given name.
     */
    public List<Country> findByCountryName(String p_countryName) {
        return UserCoreLogic.getGameEngine().getMapEditorEngine().getCountryList().stream().filter(p_country ->
                p_country.getCountryName().equals(p_countryName)
        ).collect(Collectors.toList());
    }

    /**
     * Searches for a specific country by its name, returning the first matching result. This method is designed for
     * scenarios where an exact or closest match to the country name is required from a dataset
     *
     * @param p_countryName The name of the country to be searched.
     * @return The first country that matches the given name.
     * @throws EntityNotFoundException Throws If no country with the specified name can be found.
     *
     */
    public Country findFirstByCountryName(String p_countryName) throws EntityNotFoundException {
        List<Country> l_countryList = this.findByCountryName(p_countryName);
        if (l_countryList.size() > 0)
            return l_countryList.get(0);

        throw new EntityNotFoundException(String.format("'%s' country not found", p_countryName));
    }

    /**
     * Retrieves a country based on its unique identifier.
     *
     * @param p_countryId The unique identifier (ID) of the country to find.
     * @return The country corresponding to the specified ID.
     */
    public Country findByCountryId(Integer p_countryId) {
        List<Country> l_countries = UserCoreLogic.getGameEngine().getMapEditorEngine().getCountryList().stream().filter(p_country ->
                p_country.getCountryId().equals(p_countryId)
        ).collect(Collectors.toList());
        if (!l_countries.isEmpty()) {
            return l_countries.get(0);
        } else {
            return null;
        }
    }

    /**
     * Identifies countries that share a border with the specified country.
     *
     * @param p_country The country for which neighboring countries are sought.
     * @return A list of countries that border the specified country.
     */
    public List<Country> findByNeighbourOfCountries(Country p_country) {
        return UserCoreLogic.getGameEngine().getMapEditorEngine().getCountryList().stream().filter(p_l_country ->
                !p_l_country.equals(p_country) && p_l_country.getNeighbourCountries().contains(p_country)
        ).collect(Collectors.toList());
    }

    /**
     * searches the neighboring country of the given country.
     *
     * @param p_country The country object whose neighbors are to be found.
     * @return A list of country objects representing the neighbors of the given country.
     * @throws IllegalStateException Throws if returns an empty list.
     */
    public List<Country> findCountryNeighborsAndNotOwned(Country p_country) throws IllegalStateException {
        return p_country.getNeighbourCountries().stream().filter((p_l_country) ->
                p_l_country.getOwnedBy() == null
        ).collect(Collectors.toList());
    }
}

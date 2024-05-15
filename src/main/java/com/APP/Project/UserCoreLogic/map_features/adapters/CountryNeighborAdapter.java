package com.APP.Project.UserCoreLogic.map_features.adapters;

import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.game_entities.Country;
import com.APP.Project.UserCoreLogic.exceptions.EntityNotFoundException;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;
import com.APP.Project.UserCoreLogic.map_features.MapEditorEngine;
import com.APP.Project.UserCoreLogic.Container.CountryContainer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides functionality to add or remove neighbor countries for a given country.
 * 
 * @author Rikin Dipakkumar Chauhan
 * @version 1.0
 */
public class CountryNeighborAdapter {
    
    private final MapEditorEngine d_mapEditorEngine;
    private final CountryContainer d_countryRepository;
    private final LogEntryBuffer d_logEntryBuffer;

    /**
     * Constructs a CountryNeighborAdapter object with necessary dependencies.
     */
    public CountryNeighborAdapter() {
        d_mapEditorEngine = UserCoreLogic.getGameEngine().getMapEditorEngine();;
        d_countryRepository = new CountryContainer();
        d_logEntryBuffer = LogEntryBuffer.getLogger();
    }

    /**
     * Adds a neighbor country to the given country.
     *
     * @param p_countryName       The name of the country to which the neighbor is to be added.
     * @param p_neighborCountryName The name of the neighbor country to be added.
     * @return A message indicating the successful addition of the neighbor country.
     * @throws EntityNotFoundException If the country or neighbor country is not found.
     */
    public String add(String p_countryName, String p_neighborCountryName) throws EntityNotFoundException {
        Country l_country = d_countryRepository.findFirstByCountryName(p_countryName);
        Country l_neighborCountry = d_countryRepository.findFirstByCountryName(p_neighborCountryName);
        if (!d_mapEditorEngine.getLoadingMap()) {
            d_logEntryBuffer.dataChanged("editneighbor", p_neighborCountryName + " is set as neighbor of " + p_countryName);
        }
        return this.add(l_country, l_neighborCountry);
    }

    /**
     * Adds a neighbor country to the given country.
     *
     * @param p_country        The country to which the neighbor is to be added.
     * @param p_neighborCountry The neighbor country to be added.
     * @return A message indicating the successful addition of the neighbor country.
     */
    public String add(Country p_country, Country p_neighborCountry) {
        p_country.addNeighbourCountry(p_neighborCountry);
        return String.format("Neighbor %s country added for %s!", p_neighborCountry.getCountryName(), p_country.getCountryName());
    }

    /**
     * Removes a neighbor country from the given country.
     *
     * @param p_countryName       The name of the country from which the neighbor is to be removed.
     * @param p_neighborCountryName The name of the neighbor country to be removed.
     * @return A message indicating the successful removal of the neighbor country.
     * @throws EntityNotFoundException If the country or neighbor country is not found.
     */
    public String remove(String p_countryName, String p_neighborCountryName) throws EntityNotFoundException {
        Country l_country = d_countryRepository.findFirstByCountryName(p_countryName);
        Country l_neighborCountry = d_countryRepository.findFirstByCountryName(p_neighborCountryName);

        if (!d_mapEditorEngine.getLoadingMap()) {
            d_logEntryBuffer.dataChanged("editneighbor", p_neighborCountryName + " is removed as a neighbor of " + p_countryName);
        }
        return this.remove(l_country, l_neighborCountry);
    }

    /**
     * Removes a neighbor country from the given country.
     *
     * @param p_country        The country from which the neighbor is to be removed.
     * @param p_neighborCountry The neighbor country to be removed.
     * @return A message indicating the successful removal of the neighbor country.
     */
    public String remove(Country p_country, Country p_neighborCountry) {
        List<Country> l_filteredCountry = p_country.getNeighbourCountries().stream().filter(i_p_country ->
                i_p_country.equals(p_neighborCountry)
        ).collect(Collectors.toList());

        p_country.setNeighbourCountries(l_filteredCountry);
        return String.format("Neighbor %s country removed from %s!", p_neighborCountry.getCountryName(), p_country.getCountryName());
    }
}

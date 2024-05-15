package com.APP.Project.UserCoreLogic.map_features.adapters;

import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.game_entities.Continent;
import com.APP.Project.UserCoreLogic.exceptions.EntityNotFoundException;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;
import com.APP.Project.UserCoreLogic.map_features.MapEditorEngine;
import com.APP.Project.UserCoreLogic.Container.ContinentContainer;
import com.APP.Project.UserCoreLogic.Container.CountryContainer;
import com.APP.Project.UserCoreLogic.game_entities.Country;

import java.util.List;

/**
 * Adapter class to manipulate countries on the map.
 *
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class CountryAdapter {
    
    private final MapEditorEngine d_mapEditorEngine;

    
    private final ContinentContainer d_continentRepository;

    
    private final CountryContainer d_countryRepository;

    private final LogEntryBuffer d_logEntryBuffer;

    /**
     * Constructor initializing dependencies.
     */
    public CountryAdapter() {
        d_mapEditorEngine = UserCoreLogic.getGameEngine().getMapEditorEngine();
        d_continentRepository = new ContinentContainer();
        d_countryRepository = new CountryContainer();
        d_logEntryBuffer = LogEntryBuffer.getLogger();
    }

    /**
     * Adds a country to a continent.
     *
     * @param p_countryName   Name of the country to add.
     * @param p_continentName Name of the continent to add the country to.
     * @return A message indicating the country addition.
     * @throws EntityNotFoundException if the specified continent is not found.
     */
    public String add(String p_countryName, String p_continentName) throws EntityNotFoundException {
        Country l_country = new Country(d_mapEditorEngine.getCountryList().size() + 1);
        l_country.setCountryName(p_countryName);

        Continent l_continent = d_continentRepository.findFirstByContinentName(p_continentName);
        
        l_country.setContinent(l_continent);

        
        l_continent.addCountry(l_country);
        if (!d_mapEditorEngine.getLoadingMap()) {
            d_logEntryBuffer.dataChanged("editcountry", l_country.getCountryName() + " is added to the country list of" + l_continent.getContinentName());
        }
        return String.format("%s country added!", p_countryName);
    }

    /**
     * Adds a country to a continent with additional properties.
     *
     * @param p_countryName       Name of the country to add.
     * @param p_continentName     Name of the continent to add the country to.
     * @param p_neighbourCountries List of neighboring countries.
     * @param p_xCoordinate       X coordinate of the country.
     * @param p_yCoordinate       Y coordinate of the country.
     * @return A message indicating the country addition.
     * @throws EntityNotFoundException if the specified continent is not found.
     */
    public String add(String p_countryName, String p_continentName, List<Country> p_neighbourCountries, String p_xCoordinate, String p_yCoordinate) throws EntityNotFoundException {
        boolean l_isExist = false;
        Country l_countryObject = null;
        List<Country> l_countryList = d_mapEditorEngine.getCountryList();
        for (Country l_coun : l_countryList) {
            if (l_coun.getCountryName().equalsIgnoreCase(p_countryName)) {
                l_isExist = true;
                l_countryObject = l_coun;
                break;
            }
        }
        if (l_isExist) {
            Continent l_continent = d_continentRepository.findFirstByContinentName(p_continentName);
           
            l_countryObject.setContinent(l_continent);
            
            l_countryObject.setNeighbourCountries(p_neighbourCountries);
            l_countryObject.setXCoordinate(p_xCoordinate);
            l_countryObject.setYCoordinate(p_yCoordinate);
            
            l_continent.addCountry(l_countryObject);
            if (!d_mapEditorEngine.getLoadingMap()) {
                d_logEntryBuffer.dataChanged("editcountry", l_countryObject.getCountryName() + " is added to the country list of" + l_continent.getContinentName());
            }
        } else {
            Country l_country = new Country(d_mapEditorEngine.getCountryList().size() + 1);
            l_country.setCountryName(p_countryName);
            l_country.setNeighbourCountries(p_neighbourCountries);
            l_country.setXCoordinate(p_xCoordinate);
            l_country.setYCoordinate(p_yCoordinate);
            Continent l_continent = d_continentRepository.findFirstByContinentName(p_continentName);
            
            l_country.setContinent(l_continent);

           
            l_continent.addCountry(l_country);
            if (!d_mapEditorEngine.getLoadingMap()) {
                d_logEntryBuffer.dataChanged("editcountry", l_country.getCountryName() + " is added to the country list of" + l_continent.getContinentName());
            }
        }
        return String.format("%s country added!", p_countryName);
    }

    /**
     * Adds a country to a continent using provided IDs.
     *
     * @param p_countryId    ID of the country.
     * @param p_countryName  Name of the country to add.
     * @param p_continentId  ID of the continent.
     * @return A message indicating the country addition.
     * @throws EntityNotFoundException if the specified continent is not found.
     */
    public String add(Integer p_countryId, String p_countryName, Integer p_continentId) throws EntityNotFoundException {
        Country l_country = new Country(p_countryId);
        l_country.setCountryName(p_countryName);

        Continent l_continent = d_continentRepository.findByContinentId(p_continentId);

        
        l_country.setContinent(l_continent);

        
        l_continent.addCountry(l_country);

        return String.format("%s country added!", p_countryName);
    }

    /**
     * Removes a country from the map.
     *
     * @param p_countryName Name of the country to remove.
     * @return A message indicating the country removal.
     * @throws EntityNotFoundException if the specified country is not found.
     */
    public String remove(String p_countryName) throws EntityNotFoundException {
        Country l_country = d_countryRepository.findFirstByCountryName(p_countryName);
        l_country.getContinent().removeCountry(l_country);

        List<Country> l_neighborOfCountryList = d_countryRepository.findByNeighbourOfCountries(l_country);
        for (Country l_neighborOfCountry : l_neighborOfCountryList) {
            l_neighborOfCountry.removeNeighbourCountry(l_country);
        }
        if (!d_mapEditorEngine.getLoadingMap()) {
            d_logEntryBuffer.dataChanged("editcountry", l_country.getCountryName() + " is removed to the country list of" + l_country.getContinent().getContinentName());
        }

        return String.format("%s country removed!", p_countryName);
    }
}

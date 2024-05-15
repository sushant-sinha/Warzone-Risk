package com.APP.Project.UserCoreLogic.map_features.adapters;

import com.APP.Project.UserCoreLogic.map_features.MapEditorEngine;
import com.APP.Project.UserCoreLogic.Container.ContinentContainer;
import com.APP.Project.UserCoreLogic.Container.CountryContainer;
import com.jakewharton.fliptables.FlipTable;
import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.constants.interfaces.StandaloneCommand;
import com.APP.Project.UserCoreLogic.game_entities.Continent;
import com.APP.Project.UserCoreLogic.game_entities.Country;
import com.APP.Project.UserCoreLogic.exceptions.EntityNotFoundException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidInputException;
import com.APP.Project.UserCoreLogic.exceptions.ResourceNotFoundException;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;

import java.util.*;

/**
 * The ShowMapAdapter class provides methods to display continent-country content and neighbor countries of each country in a map.
 *
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class ShowMapAdapter implements StandaloneCommand {
    MapEditorEngine d_mapEditorEngine;
    ContinentContainer d_continentRepository;
    CountryContainer d_countryRepository;
    List<Continent> d_continentList;
    List<Country> d_countryList;
    Map<String, List<String>> d_continentCountryMap;
    private final LogEntryBuffer d_logEntryBuffer;

    /**
     * Constructs a ShowMapAdapter object.
     *
     * @throws EntityNotFoundException if an entity is not found.
     */
    public ShowMapAdapter() throws EntityNotFoundException {
        d_mapEditorEngine = UserCoreLogic.getGameEngine().getMapEditorEngine();
        d_continentList = d_mapEditorEngine.getContinentList();
        d_countryList = d_mapEditorEngine.getCountryList();
        d_continentCountryMap = d_mapEditorEngine.getContinentCountryMap();
        d_continentRepository = new ContinentContainer();
        d_countryRepository = new CountryContainer();
        d_logEntryBuffer = LogEntryBuffer.getLogger();
    }

     /**
     * Displays the continent-country content in a formatted table.
     *
     * @return a formatted table representing the continent-country content.
     */
    public String showContinentCountryContent() {

        
        String[] l_header = {"Continent Name", "Control Value", "Countries"};
        List<List<String>> l_mapContent = new ArrayList<>();

        for (Map.Entry<String, List<String>> l_entry : d_continentCountryMap.entrySet()) {
            ArrayList<String> l_continentsList = new ArrayList<>();
            l_continentsList.add(l_entry.getKey());
            try {
                
                Continent l_continent = d_continentRepository.findFirstByContinentName(l_entry.getKey());
                l_continentsList.add(String.valueOf(l_continent.getContinentControlValue()));

                
                List<String> l_sortedCountryList = new ArrayList<>(l_entry.getValue());
                Collections.sort(l_sortedCountryList);
                String l_continentCountries = String.join(",", l_sortedCountryList);
                l_continentsList.add(l_continentCountries);
                l_mapContent.add(l_continentsList);

            } catch (EntityNotFoundException p_e) {
                p_e.printStackTrace();
            }
        }

        
        String[][] l_continentMapMatrix = new String[l_mapContent.size()][];
        for (int i = 0; i < l_mapContent.size(); i++) {
            List<String> l_singleContinentContent = l_mapContent.get(i);
            l_continentMapMatrix[i] = l_singleContinentContent.toArray(new String[l_singleContinentContent.size()]);
        }

        return FlipTable.of(l_header, l_continentMapMatrix);
    }

    /**
     * Displays the neighbor countries of each country in a formatted table.
     *
     * @return a formatted table representing the neighbor countries of each country.
     */    
    public String showNeighbourCountries() {
        LinkedList<String> l_countryNames = new LinkedList<>();
        String[][] l_neighbourCountryMatrix = new String[d_countryList.size() + 1][d_countryList.size() + 1];

        
        for (Country l_country : d_countryList) {
            l_countryNames.add(l_country.getCountryName());
        }
        l_neighbourCountryMatrix[0][0] = "COUNTRIES";
        
        for (int l_row = 1; l_row < l_neighbourCountryMatrix.length; l_row++) {
            String l_name = l_countryNames.pollFirst();
            l_neighbourCountryMatrix[l_row][0] = l_name;
            l_neighbourCountryMatrix[0][l_row] = l_name;
        }

        
        for (int l_row = 1; l_row < l_neighbourCountryMatrix.length; l_row++) {
            Country l_countryRow;
            try {
                l_countryRow = d_countryRepository.findFirstByCountryName(l_neighbourCountryMatrix[l_row][0]);
                List<Country> l_countryNeighbourList = l_countryRow.getNeighbourCountries();
                for (int l_col = 1; l_col < l_neighbourCountryMatrix.length; l_col++) {
                    Country l_countryColumn = d_countryRepository.findFirstByCountryName(l_neighbourCountryMatrix[0][l_col]);
                    if (l_countryRow.equals(l_countryColumn) || l_countryNeighbourList.contains(l_countryColumn)) {
                        l_neighbourCountryMatrix[l_row][l_col] = "X";
                    } else {
                        l_neighbourCountryMatrix[l_row][l_col] = "O";
                    }
                }
            } catch (EntityNotFoundException p_e) {
                p_e.printStackTrace();
            }
        }

        String[] l_countryCountHeader = new String[l_neighbourCountryMatrix.length];
        for (int i = 0; i < l_countryCountHeader.length; i++) {
            l_countryCountHeader[i] = "C" + i;
        }

        return FlipTable.of(l_countryCountHeader, l_neighbourCountryMatrix);
    }

     /**
     * Executes the command and returns the result.
     *
     * @param p_commandValues list of command values
     * @return the result of the execution
     * @throws EntityNotFoundException if an entity is not found
     * @throws ResourceNotFoundException if a resource is not found
     * @throws InvalidInputException if the input is invalid
     */    
    @Override
    public String execute(List<String> p_commandValues) throws EntityNotFoundException, ResourceNotFoundException, InvalidInputException {
        String l_logResponse = "";
        if (!this.d_continentCountryMap.isEmpty() || !this.d_countryList.isEmpty()) {
            l_logResponse = this.showContinentCountryContent() + "\n" + this.showNeighbourCountries();
            
            d_logEntryBuffer.dataChanged("showmap", l_logResponse);
            return l_logResponse;
        } else {
            throw new EntityNotFoundException("Please select file to show!");
        }
    }
}

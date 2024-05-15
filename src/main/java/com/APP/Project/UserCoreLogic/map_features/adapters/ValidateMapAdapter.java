package com.APP.Project.UserCoreLogic.map_features.adapters;

import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.constants.interfaces.StandaloneCommand;
import com.APP.Project.UserCoreLogic.game_entities.Continent;
import com.APP.Project.UserCoreLogic.game_entities.Country;
import com.APP.Project.UserCoreLogic.exceptions.EntityNotFoundException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidMapException;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;
import com.APP.Project.UserCoreLogic.map_features.MapEditorEngine;
import com.APP.Project.UserCoreLogic.Container.CountryContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * This class represents a ValidateMapAdapter which implements StandaloneCommand.
 * It provides methods to validate various aspects of a map including continent connectivity,
 * overall map connectivity, and control values of continents.
 * 
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class ValidateMapAdapter implements StandaloneCommand {
    /**
     * Engine to store and retrieve map data.
     */
    private final MapEditorEngine d_mapEditorEngine;

    private final LogEntryBuffer d_logEntryBuffer;

    /**
     * Constructs a ValidateMapAdapter object.
     */
    public ValidateMapAdapter() {
        d_mapEditorEngine = UserCoreLogic.getGameEngine().getMapEditorEngine();
        d_logEntryBuffer = LogEntryBuffer.getLogger();
    }

    /**
     * Checks if the continent forms a connected subgraph.
     * 
     * @return true if continent forms a connected subgraph, false otherwise.
     * @throws EntityNotFoundException if entity not found.
     */
    public boolean isContinentConnectedSubgraph() throws EntityNotFoundException {
        if (d_mapEditorEngine.getContinentList().size() > 1) {
            boolean l_isInvalid = false;
            String l_continentName;
            List<String> l_countriesIntoContinent;
            CountryContainer l_countryRepository = new CountryContainer();
            int l_totalContinent = d_mapEditorEngine.getContinentList().size();
            int l_compareTotalContinent = 0;
            Country l_foundCountry = null;

            Map<String, List<String>> l_continentCountryMap = d_mapEditorEngine.getContinentCountryMap();
            for (Map.Entry<String, List<String>> entry : l_continentCountryMap.entrySet()) {
                
                l_continentName = entry.getKey();
                l_countriesIntoContinent = entry.getValue();
                int l_otherContinentNeighbour = 0;

                
                for (String l_countryNameCompare : l_countriesIntoContinent) {
                    try {
                        l_foundCountry = l_countryRepository.findFirstByCountryName(l_countryNameCompare);
                    } catch (EntityNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (l_foundCountry == null) {
                        continue;
                    }
                    List<Country> l_neighbourCountries = l_foundCountry.getNeighbourCountries();

                    for (Country l_country : l_neighbourCountries) {
                        Continent l_continent = l_country.getContinent();
                        String ContinentName = l_continent.getContinentName();
                        if (!(ContinentName.equals(l_continentName))) {
                            l_otherContinentNeighbour++;
                            break;
                        }
                    }

                    if (l_otherContinentNeighbour > 0) {
                        l_compareTotalContinent++;
                        break;
                    }
                }
            }
            
            if (l_compareTotalContinent == l_totalContinent) {
                l_isInvalid = true;
            }
            return l_isInvalid;
        } else {
            return true;
        }
    }

    /**
     * Checks if the map forms a connected graph.
     * 
     * @return true if map forms a connected graph, false otherwise.
     */
    public boolean isMapConnectedGraph() {
        boolean l_isValid = false;
        int l_connectedGraphCount = 0;

        List<Country> l_countryList = d_mapEditorEngine.getCountryList();
        for (int i = 0; i < l_countryList.size(); i++) {
            List<Country> l_visitedCountry = new ArrayList<>();
            Stack<Country> l_stack = new Stack<>();
            Country l_country = l_countryList.get(i);
            l_stack.push(l_country);

            while (!l_stack.isEmpty()) {
                Country l_countryGet = l_stack.pop();
                l_visitedCountry.add(l_countryGet);
                List<Country> l_neighbourCountries = l_countryGet.getNeighbourCountries();
                for (Country l_pushCountry : l_neighbourCountries) {
                    if (!l_stack.contains(l_pushCountry)) {
                        int l_counter = 0;
                        for (Country l_compareCountry : l_visitedCountry) {
                            if (l_pushCountry.equals(l_compareCountry)) {
                                l_counter++;
                            }
                        }
                        if (l_counter == 0) {
                            l_stack.push(l_pushCountry);
                        }
                    }
                }
            }
            
            int compareCounter = 0;
            for (Country l_compareCountry : l_countryList) {
                for (Country l_compare2 : l_visitedCountry) {
                    if (l_compare2.equals(l_compareCountry)) {
                        compareCounter++;
                    }
                }
            }
            if (compareCounter == l_countryList.size()) {
                ++l_connectedGraphCount;
            }
        }
        if (l_connectedGraphCount == l_countryList.size()) {
            l_isValid = true;
        }
        return l_isValid;
    }

    /**
     * Validates the control value of continents.
     * 
     * @param p_continentList The list of continents to validate.
     * @return true if control values are valid for all continents, false otherwise.
     */
    private boolean validationControlValue(List<Continent> p_continentList) {
        boolean l_isValid = true;

        for (Continent l_continent : p_continentList) {
            if (l_continent.getContinentControlValue() < 0) {
                l_isValid = false;
                break;
            }
        }
        return l_isValid;
    }

    /**
     * Executes the command to validate the map.
     * 
     * @param p_commandValues The command values.
     * @return A message indicating the result of map validation.
     * @throws InvalidMapException if map is invalid.
     * @throws EntityNotFoundException if entity not found.
     */
    @Override
    public String execute(List<String> p_commandValues) throws InvalidMapException, EntityNotFoundException {
        String l_logResponse = "\n---VALIDATEMAP---\n";
        
        if (d_mapEditorEngine.getContinentList().size() > 0) {
            
            if (validationControlValue(d_mapEditorEngine.getContinentList())) {
                
                if (d_mapEditorEngine.getCountryList().size() > 1) {
                    
                    if (d_mapEditorEngine.getCountryList().size() >= d_mapEditorEngine.getContinentList().size()) {
                        
                        if (isContinentConnectedSubgraph()) {
                            
                            if (isMapConnectedGraph()) {
                                d_logEntryBuffer.dataChanged("validatemap", l_logResponse + "Map validation passed successfully!");
                                return "Map validation passed successfully!";
                            } else {
                                d_logEntryBuffer.dataChanged("validatemap", l_logResponse + "map must be a connected graph!");
                                throw new InvalidMapException("map must be a connected graph!");
                            }
                        } else {
                            d_logEntryBuffer.dataChanged("validatemap", l_logResponse + "Continent must be a connected sub-graph!");
                            throw new InvalidMapException("Continent must be a connected sub-graph!");
                        }
                    } else {
                        d_logEntryBuffer.dataChanged("validatemap", l_logResponse + "Total continents must be lesser or equal to the countries!");
                        throw new InvalidMapException("Total continents must be lesser or equal to the countries!");
                    }
                } else {
                    d_logEntryBuffer.dataChanged("validatemap", l_logResponse + "At least one country required!");
                    throw new InvalidMapException("At least one country required!");
                }
            } else {
                d_logEntryBuffer.dataChanged("validatemap", l_logResponse + "ControlValue is not valid!");
                throw new InvalidMapException("ControlValue is not valid!");
            }
        } else {
            d_logEntryBuffer.dataChanged("validatemap", l_logResponse + "At least one continent required!");
            throw new InvalidMapException("At least one continent required!");
        }
    }

    /**
     * Executes the command to validate the map with a specific head command.
     * 
     * @param p_commandValues The command values.
     * @param p_headCommand The head command.
     * @return A message indicating the result of map validation.
     * @throws InvalidMapException if map is invalid.
     * @throws EntityNotFoundException if entity not found.
     */
    public String execute(List<String> p_commandValues, String p_headCommand) throws InvalidMapException, EntityNotFoundException {
    
        if (d_mapEditorEngine.getContinentList().size() > 0) {
            
            if (validationControlValue(d_mapEditorEngine.getContinentList())) {
                
                if (d_mapEditorEngine.getCountryList().size() > 1) {
                    
                    if (d_mapEditorEngine.getCountryList().size() >= d_mapEditorEngine.getContinentList().size()) {
                        
                        if (isContinentConnectedSubgraph()) {
                            
                            if (isMapConnectedGraph()) {
                                return "Map validation passed successfully!";
                            } else {
                                throw new InvalidMapException("map must be a connected graph!");
                            }
                        } else {
                            throw new InvalidMapException("Continent must be a connected sub-graph!");
                        }
                    } else {
                        throw new InvalidMapException("Total continents must be lesser or equal to the countries!");
                    }
                } else {
                    throw new InvalidMapException("At least one country required!");
                }
            } else {
                throw new InvalidMapException("ControlValue is not valid!");
            }
        } else {
            throw new InvalidMapException("At least one continent required!");
        }
    }
}
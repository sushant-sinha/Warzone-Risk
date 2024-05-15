package com.APP.Project.UserCoreLogic.map_features;

import com.APP.Project.UserCoreLogic.Container.CountryContainer;
import com.APP.Project.UserCoreLogic.GameEngine;
import com.APP.Project.UserCoreLogic.constants.interfaces.Engine;
import com.APP.Project.UserCoreLogic.constants.interfaces.JSONable;
import com.APP.Project.UserCoreLogic.game_entities.Continent;
import com.APP.Project.UserCoreLogic.game_entities.Country;
import com.APP.Project.UserCoreLogic.exceptions.EntityNotFoundException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidGameException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * This class represents the engine for editing maps in a game.
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class MapEditorEngine implements Engine, JSONable {
    private List<Continent> d_continentList;
    private HashMap<String, String> d_MapDetails;
    private boolean d_isLoadingMap = false;

    /*
     * Constructor for MapEditorEngine.
     */
    public MapEditorEngine() {
        this.initialise();
    }

    /*
     * Initializes the MapEditorEngine.
     */
    public void initialise() {
        d_continentList = new ArrayList<>();
        
        Continent.resetSerialNumber();
        Country.resetSerialNumber();
    }

    /**
     * Retrieves the list of continents.
     * @return The list of continents.
     */
    public List<Continent> getContinentList() {
        return d_continentList;
    }

    /**
     * Sets the list of continents.
     * @param p_continentList The list of continents to be set.
     */
    public void setContinentList(List<Continent> p_continentList) {
        d_continentList = p_continentList;
    }

    /**
     * Retrieves the list of countries.
     * @return The list of countries.
     */
    public ArrayList<Country> getCountryList() {
        ArrayList<Country> l_countries = new ArrayList<>();
        for (Continent l_continent : d_continentList) {
            for (Country l_country : l_continent.getCountryList()) {
                if (!l_countries.contains(l_country)) {
                    l_countries.add(l_country);
                }
            }
        }
        return l_countries;
    }

    /**
     * Retrieves the map of countries and their neighbors.
     * @return The map of country IDs and their neighbor country IDs.
     */
    public Map<Integer, Set<Integer>> getCountryNeighbourMap() {
        Map<Integer, Set<Integer>> l_continentCountryMap = new HashMap<>();
        ArrayList<Country> l_countries = this.getCountryList();
        for (Country l_country : l_countries) {
            Set<Integer> l_neighborCountryIdList = new HashSet<>();
            for (Country l_neighborCountry : l_country.getNeighbourCountries()) {
                l_neighborCountryIdList.add(l_neighborCountry.getCountryId());
            }
            l_continentCountryMap.put(l_country.getCountryId(), l_neighborCountryIdList);
        }
        return l_continentCountryMap;
    }

    /**
     * Retrieves the map of continents and their countries.
     * @return The map of continents and their respective country names.
     * @throws EntityNotFoundException If a continent does not contain any country.
     */
    public Map<String, List<String>> getContinentCountryMap() throws EntityNotFoundException {
        Map<String, List<String>> l_continentCountryMap = new HashMap<>();
        for (Continent l_continent : d_continentList) {
            if (!l_continent.getCountryList().isEmpty()) {
                for (Country l_country : l_continent.getCountryList()) {
                    String continentName = l_continent.getContinentName();
                    List<String> l_countryNames;
                    if (l_continentCountryMap.containsKey(continentName)) {
                        l_countryNames = l_continentCountryMap.get(continentName);
                    } else {
                        l_countryNames = new ArrayList<>();
                    }
                    l_countryNames.add(l_country.getCountryName());
                    l_continentCountryMap.put(continentName, l_countryNames);
                }
            } else {
                throw new EntityNotFoundException("Add minimum one country in a continent!");
            }
        }
        return l_continentCountryMap;
    }

    /**
     * Adds a continent to the list of continents.
     * @param p_continent The continent to be added.
     */
    public void addContinent(Continent p_continent) {
        d_continentList.add(p_continent);
    }

    /**
     * Retrieves the loading status of the map.
     * @return The loading status of the map.
     */
    public boolean getLoadingMap() {
        return d_isLoadingMap;
    }

    /**
     * Sets the loading status of the map.
     * @param p_loadingMap The loading status of the map to be set.
     */
    public void setLoadingMap(boolean p_loadingMap) {
        d_isLoadingMap = p_loadingMap;
    }

    /*
     * Shuts down the MapEditorEngine.
     */
    public void shutdown() {
        // No threads created by MapEditorEngine.
    }

    /**
     * Converts the MapEditorEngine object to a JSON object.
     * @return The JSON representation of the MapEditorEngine.
     */
    @Override
    public JSONObject toJSON() {
        JSONObject l_mapEditorEngineJSON = new JSONObject();
        JSONArray l_continentJSONList = new JSONArray();
        for (Continent l_continent : getContinentList()) {
            l_continentJSONList.put(l_continent.toJSON());
        }
        l_mapEditorEngineJSON.put("continents", l_continentJSONList);

        
        JSONObject l_neighborCountryJSON = new JSONObject();
        for (Country l_country : getCountryList()) {
            JSONArray l_countryNeighbors = new JSONArray();
            for (Country l_neighbourCountry : l_country.getNeighbourCountries()) {
                l_countryNeighbors.put(l_neighbourCountry.getCountryName());
            }
            l_neighborCountryJSON.put(l_country.getCountryName(), l_countryNeighbors);
        }
        l_mapEditorEngineJSON.put("neighborCountryMappings", l_neighborCountryJSON);
        return l_mapEditorEngineJSON;
    }

    /**
     * Constructs a MapEditorEngine object from a JSON object.
     * @param p_jsonObject The JSON object representing the MapEditorEngine.
     * @param p_gameEngine The game engine associated with the MapEditorEngine.
     * @return The constructed MapEditorEngine object.
     * @throws InvalidGameException If the game is invalid.
     */
    public static MapEditorEngine fromJSON(JSONObject p_jsonObject, GameEngine p_gameEngine) throws InvalidGameException {
        CountryContainer l_countryRepository = new CountryContainer();

        MapEditorEngine l_mapEditorEngine = new MapEditorEngine();
        p_gameEngine.setMapEditorEngine(l_mapEditorEngine);

        JSONArray l_continentJSONList = p_jsonObject.getJSONArray("continents");
        for (int l_continentIndex = 0; l_continentIndex < l_continentJSONList.length(); l_continentIndex++) {
            JSONObject l_continentJSON = l_continentJSONList.getJSONObject(l_continentIndex);
            
            Continent l_continent = Continent.fromJSON(l_continentJSON);
            l_mapEditorEngine.addContinent(l_continent);
        }

        
        JSONObject l_neighborCountryJSON = p_jsonObject.getJSONObject("neighborCountryMappings");
        Set<String> l_countryList = l_neighborCountryJSON.keySet();
        for (String l_countryName : l_countryList) {
            try {
                Country l_targetCountry = l_countryRepository.findFirstByCountryName(l_countryName);
                JSONArray l_countryNeighbors = l_neighborCountryJSON.getJSONArray(l_countryName);
                for (int l_neighborCountryIndex = 0; l_neighborCountryIndex < l_countryNeighbors.length(); l_neighborCountryIndex++) {
                    String l_neighborCountryName = l_countryNeighbors.getString(l_neighborCountryIndex);
                    try {
                        Country l_targetNeighborCountry = l_countryRepository.findFirstByCountryName(l_neighborCountryName);
                        
                        l_targetCountry.addNeighbourCountry(l_targetNeighborCountry);
                    } catch (EntityNotFoundException p_entityNotFoundException) {
                        throw new InvalidGameException(String.format("Neighbor country of %s with name %s not found!", l_countryName, l_neighborCountryName));
                    }
                }
            } catch (EntityNotFoundException p_entityNotFoundException) {
                throw new InvalidGameException(String.format("Country with name %s not found!", l_countryName));
            }
        }
        return l_mapEditorEngine;

    }

    /**
     * Retrieves the map details.
     * @return The map details.
     */
    public HashMap<String, String> getMapDetails() {
        return d_MapDetails;
    }

    /**
     * Sets the map details.
     * @param p_MapDetails The map details to be set.
     */
    public void setMapDetails(HashMap<String, String> p_MapDetails) {
        d_MapDetails = p_MapDetails;
    }
}

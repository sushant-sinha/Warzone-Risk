package com.APP.Project.UserCoreLogic.map_features.adapters;

import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.game_entities.Continent;
import com.APP.Project.UserCoreLogic.exceptions.EntityNotFoundException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidInputException;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;
import com.APP.Project.UserCoreLogic.map_features.MapEditorEngine;
import com.APP.Project.UserCoreLogic.Container.ContinentContainer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Adapter class for manipulating continents in the map editor.
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class ContinentAdapter {
    
    private final MapEditorEngine d_mapEditorEngine;
    private final ContinentContainer d_continentRepository;
    private final LogEntryBuffer d_logEntryBuffer;

    /**
     * Constructor for ContinentAdapter.
     */    
    public ContinentAdapter() {
        d_mapEditorEngine = UserCoreLogic.getGameEngine().getMapEditorEngine();
        d_continentRepository = new ContinentContainer();
        d_logEntryBuffer = LogEntryBuffer.getLogger();
    }

    /**
     * Add a new continent.
     * @param p_continentName Name of the continent to be added.
     * @param p_countryValue Control value of the continent.
     * @return Confirmation message.
     * @throws InvalidInputException if the input is invalid.
     */
    public String add(String p_continentName, String p_countryValue) throws InvalidInputException {
        try {
            int l_parsedControlValue = Integer.parseInt(p_countryValue);
            Continent l_continent = new Continent();
            l_continent.setContinentName(p_continentName);
            l_continent.setContinentControlValue(l_parsedControlValue);
            d_mapEditorEngine.addContinent(l_continent);
            if (!d_mapEditorEngine.getLoadingMap()) {
                
                d_logEntryBuffer.dataChanged("editcontinent", l_continent.getContinentName() + " is added to the list!");
            }
            return String.format("%s continent added!", p_continentName);
        } catch (Exception e) {
            throw new InvalidInputException("Continent control value is not in valid format!");
        }
    }

    /**
     * Remove a continent.
     * @param p_continentName Name of the continent to be removed.
     * @return Confirmation message.
     * @throws EntityNotFoundException if the continent is not found.
     */
    public String remove(String p_continentName) throws EntityNotFoundException {
        Continent l_continent = d_continentRepository.findFirstByContinentName(p_continentName);
        
        List<Continent> l_filteredContinentList = d_mapEditorEngine.getContinentList().stream()
                .filter(p_continent -> !p_continent.equals(l_continent)
                ).collect(Collectors.toList());
        d_mapEditorEngine.setContinentList(l_filteredContinentList);
        if (!d_mapEditorEngine.getLoadingMap()) {
            
            d_logEntryBuffer.dataChanged("editcontinent", l_continent.getContinentName() + " is removed to the list!");
        }
        return String.format("%s continent removed!", p_continentName);
    }
}

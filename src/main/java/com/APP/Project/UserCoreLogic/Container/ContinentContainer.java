package com.APP.Project.UserCoreLogic.Container;

import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.game_entities.Continent;
import com.APP.Project.UserCoreLogic.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class search for the Continent entity.
 *
 * @author Bhoomiben Bhatt
 * @version 1.0
 */
public class ContinentContainer {
    /**
     * Finds the continent using it's continent name.
     *
     * @param p_continentName Value of the name of continent.
     * @return Value of the list of matched continents.
     */
    public List<Continent> findByContinentName(String p_continentName) {
        return UserCoreLogic.getGameEngine().getMapEditorEngine().getContinentList().stream().filter(p_continent ->
                p_continent.getContinentName().equals(p_continentName)
        ).collect(Collectors.toList());
    }

    /**
     * Performs a search for a continent by its name, returning the first match found.
     *
     * @param p_continentName Value of the name of continent.
     * @return Value of the very first matched continents.
     * @throws EntityNotFoundException Throws If no continent with the specified name can be found.
     */
    public Continent findFirstByContinentName(String p_continentName) throws EntityNotFoundException {
        List<Continent> l_continentList = this.findByContinentName(p_continentName);
        if (l_continentList.size() > 0)
            return l_continentList.get(0);
        throw new EntityNotFoundException(String.format("'%s' continent not found", p_continentName));
    }

    /**
     * Retrieves a continent based on its unique identifier.
     *
     * @param p_continentId The unique identifier of the continent to be found.
     * @return Value of the first matched continents.
     * @throws EntityNotFoundException Throws If no continent with the given ID can be located.
     */
    public Continent findByContinentId(Integer p_continentId) throws EntityNotFoundException {
        List<Continent> l_continentList = UserCoreLogic.getGameEngine().getMapEditorEngine().getContinentList().stream().filter(p_continent ->
                p_continent.getContinentId().equals(p_continentId)
        ).collect(Collectors.toList());
        if (!l_continentList.isEmpty()) {
            return l_continentList.get(0);
        }
        throw new EntityNotFoundException(String.format("Continent with %s id not found!", p_continentId));
    }
}

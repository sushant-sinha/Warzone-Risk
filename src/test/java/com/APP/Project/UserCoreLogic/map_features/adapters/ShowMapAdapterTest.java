package com.APP.Project.UserCoreLogic.map_features.adapters;

import com.APP.Project.UserCoreLogic.exceptions.*;
import com.jakewharton.fliptables.FlipTable;
import com.APP.Project.Main;
import com.APP.Project.UserCoreLogic.UserCoreLogic;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This class provides unit tests for the ShowMapAdapter class.
 * It tests the functionality of displaying continent-country content and neighbor countries.
 *
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class ShowMapAdapterTest {
    private static Main d_application = new Main();
    private ShowMapAdapter d_showMapService;

    /**
     * Runs before any test cases are executed. It handles application startup,
     * initializes the game engine, and loads a test map.
     *
     * @throws AbsentTagException      if a required tag is absent in the map file.
     * @throws InvalidMapException     if the map file is invalid.
     * @throws ResourceNotFoundException if the specified resource is not found.
     * @throws InvalidInputException   if the input provided is invalid.
     * @throws EntityNotFoundException if an entity is not found.
     * @throws URISyntaxException      if the URI syntax is invalid.
     * @throws IOException             if an I/O error occurs.
     */
    @BeforeClass
    public static void beforeClass() throws AbsentTagException, InvalidMapException, ResourceNotFoundException, InvalidInputException, EntityNotFoundException, URISyntaxException, IOException {
        d_application.handleApplicationStartup();
        
        UserCoreLogic.getGameEngine().initialise();

        EditMapService l_editMapService = new EditMapService();

        URL l_testFilePath = ShowMapAdapterTest.class.getClassLoader().getResource("test_map_files/test_map.map");
        assertNotNull(l_testFilePath);
        
        String l_url = new URI(l_testFilePath.getPath()).getPath();
        l_editMapService.handleLoadMap(l_url);
    }

    /**
     * Runs before each test case. It initializes the ShowMapAdapter object.
     *
     * @throws EntityNotFoundException if an entity is not found.
     */
    @Before
    public void before() throws EntityNotFoundException {
        d_showMapService = new ShowMapAdapter();
    }

    /**
     * Tests the functionality of displaying continent-country content.
     */
    @Test
    public void testShowContinentCountryContentTest() {
        String[] l_header = {"Continent Name", "Control Value", "Countries"};
        String[][] l_mapMatrix = {
                {"Earth", "10", "Earth-Atlantic,Earth-SouthAmerica,Earth-SouthPole"},
                {"Venus", "8", "Venus-East,Venus-South,Venus-Southwest"},
                {"Mercury", "6", "Mercury-East,Mercury-North,Mercury-South,Mercury-West"}
        };
        String l_mapTable = FlipTable.of(l_header, l_mapMatrix);
        String l_mapData = d_showMapService.showContinentCountryContent();
        assertNotNull(l_mapData);
        assertEquals(l_mapTable, l_mapData);
    }

    /**
     * Tests the functionality of displaying neighbor countries.
     */
    @Test
    public void testShowNeighbourCountriesTest() {
        String[][] l_neighbourMatrix = {
                {"COUNTRIES", "Mercury-South", "Mercury-East", "Mercury-West", "Mercury-North", "Venus-South", "Venus-East", "Venus-Southwest", "Earth-SouthPole", "Earth-SouthAmerica", "Earth-Atlantic"},
                {"Mercury-South", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O"},
                {"Mercury-East", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O"},
                {"Mercury-West", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O"},
                {"Mercury-North", "O", "X", "X", "X", "X", "O", "O", "O", "O", "O"},
                {"Venus-South", "O", "O", "O", "X", "X", "X", "X", "O", "O", "O"},
                {"Venus-East", "O", "O", "O", "O", "X", "X", "X", "O", "O", "O"},
                {"Venus-Southwest", "O", "O", "O", "O", "X", "X", "X", "O", "O", "O"},
                {"Earth-SouthPole", "O", "O", "X", "O", "O", "O", "O", "X", "X", "O"},
                {"Earth-SouthAmerica", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X"},
                {"Earth-Atlantic", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X"}

        };
        String[] l_countryCountHeader = new String[l_neighbourMatrix.length];
        for (int i = 0; i < l_countryCountHeader.length; i++) {
            l_countryCountHeader[i] = "C" + i;
        }
        String l_countryData = d_showMapService.showNeighbourCountries();
        String l_countryTable = FlipTable.of(l_countryCountHeader, l_neighbourMatrix);
        assertNotNull(l_countryData);
        assertEquals(l_countryTable, l_countryData);
    }
}
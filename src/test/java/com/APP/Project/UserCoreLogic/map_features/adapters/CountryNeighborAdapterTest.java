package com.APP.Project.UserCoreLogic.map_features.adapters;

import com.APP.Project.Main;
import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.exceptions.EntityNotFoundException;
import com.APP.Project.UserCoreLogic.exceptions.UserCoreLogicException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertNotNull;

/**
 * This class represents a test suite for the CountryAdapter class.
 * It contains test cases for adding and removing countries from continents.
 *
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class CountryNeighborAdapterTest {
    private static Main d_Application = new Main();
    private static CountryNeighborAdapter d_CountryNeighbourService;
    private EditMapService d_editMapService;
    private URL d_testFilePath;

    /**
     * Method to perform setup before running the test suite.
     */
    @BeforeClass
    public static void beforeClass() {
        d_Application.handleApplicationStartup();
        UserCoreLogic.getInstance().initialise();
        d_CountryNeighbourService = new CountryNeighborAdapter();
    }

    /**
     * Method to perform setup before each test case.
     *
     * @throws UserCoreLogicException if an error occurs in UserCoreLogic
     * @throws URISyntaxException     if an error occurs in URI syntax
     */
    @Before
    public void beforeTestCase() throws UserCoreLogicException, URISyntaxException {
        UserCoreLogic.getGameEngine().initialise();
        d_editMapService = new EditMapService();
        d_testFilePath = getClass().getClassLoader().getResource("test_map_files/test_map.map");
        assertNotNull(d_testFilePath);
        String l_url = new URI(d_testFilePath.getPath()).getPath();
        d_editMapService.handleLoadMap(l_url);
    }

    /**
     * Test case to validate behavior when wrong country values are provided.
     *
     * @throws EntityNotFoundException if entity is not found
     */
    @Test(expected = EntityNotFoundException.class)
    public void testWrongCountryValues() throws EntityNotFoundException {
        
        d_CountryNeighbourService.add("ABC", "DEF");

        
        d_CountryNeighbourService.add("Mercury-South", "DEF");

        
        d_CountryNeighbourService.add("ABC", "Mercury-West");
    }

    /**
     * Test case to validate the addition and removal of countries.
     *
     * @throws EntityNotFoundException if entity is not found
     */
    @Test(expected = Test.None.class)
    public void testAdd() throws EntityNotFoundException {
        String l_addResponse = d_CountryNeighbourService.add("Mercury-South", "Mercury-West");
        assertNotNull(l_addResponse);

        String l_removeResponse = d_CountryNeighbourService.remove("Mercury-South", "Mercury-West");

        assertNotNull(l_removeResponse);
    }
}
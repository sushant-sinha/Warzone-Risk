package com.APP.Project.UserCoreLogic.map_features.adapters;

import com.APP.Project.Main;
import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.game_entities.Continent;
import com.APP.Project.UserCoreLogic.exceptions.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * This class contains test cases for the CountryAdapter class.
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class CountryAdapterTest {
    private static Main d_Application = new Main();
    private static CountryAdapter d_CountryService;
    private List<Continent> d_continentList;
    private EditMapService d_editMapService;
    private URL d_testFilePath;

    /**
     * Executes once before any test cases are executed.
     * It initializes necessary resources for testing.
     */
    @BeforeClass
    public static void beforeTestClass() {
        d_Application.handleApplicationStartup();
        UserCoreLogic.getInstance().initialise();
        d_CountryService = new CountryAdapter();
    }

    /**
     * Executes before each test case.
     * It loads the map file and prepares necessary resources for testing.
     * @throws AbsentTagException if a required tag is absent in the map file.
     * @throws InvalidMapException if the map file is invalid.
     * @throws ResourceNotFoundException if a required resource is not found.
     * @throws InvalidInputException if the input provided is invalid.
     * @throws EntityNotFoundException if a required entity is not found.
     * @throws URISyntaxException if a URI syntax exception occurs.
     */
    @Before
    public void beforeTestCase() throws AbsentTagException, InvalidMapException, ResourceNotFoundException, InvalidInputException, EntityNotFoundException, URISyntaxException {
        d_editMapService = new EditMapService();
        d_testFilePath = getClass().getClassLoader().getResource("test_map_files/test_map.map");
        assertNotNull(d_testFilePath);
        String l_url = new URI(d_testFilePath.getPath()).getPath();
        d_editMapService.handleLoadMap(l_url);
        d_continentList = UserCoreLogic.getGameEngine().getMapEditorEngine().getContinentList();
    }

    /**
     * Tests the scenario where an invalid continent name is provided.
     * It is expected to throw EntityNotFoundException.
     * @throws EntityNotFoundException if the specified entity is not found.
     */
    @Test(expected = EntityNotFoundException.class)
    public void testInvalidContinentName() throws EntityNotFoundException {
        d_CountryService.add("India", "ABC");
    }

    /**
     * Tests the scenario of adding and removing a country.
     * It is expected that no exception is thrown during the execution.
     * @throws EntityNotFoundException if the specified entity is not found.
     */
    @Test(expected = Test.None.class)
    public void testAddRemoveCountry()
            throws EntityNotFoundException {

        String l_continentName = d_continentList.get(0).getContinentName();
        String l_responseStringAddOp = d_CountryService.add("India", l_continentName);
        assertNotNull(l_responseStringAddOp);

        String l_responseStringRemoveOp = d_CountryService.remove("India");
        assertNotNull(l_responseStringRemoveOp);
    }
}
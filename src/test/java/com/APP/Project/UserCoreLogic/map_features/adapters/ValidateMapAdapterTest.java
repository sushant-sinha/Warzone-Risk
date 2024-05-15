package com.APP.Project.UserCoreLogic.map_features.adapters;

import com.APP.Project.Main;
import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.exceptions.InvalidMapException;
import com.APP.Project.UserCoreLogic.exceptions.UserCoreLogicException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This class provides unit tests for the ValidateMapAdapter class.
 *
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class ValidateMapAdapterTest {
    private static Main d_application = new Main();
    private ValidateMapAdapter d_validateMapService;
    private EditMapService d_editMapService;
    private URL d_testFilePath;

    /**
     * This method is executed once before any tests are run.
     */
    @BeforeClass
    public static void before() {
        d_application.handleApplicationStartup();
        UserCoreLogic.getInstance().initialise();

    }

    /**
     * This method is executed before each test method.
     */
    @Before
    public void beforeTest() {
        d_editMapService = new EditMapService();
        d_validateMapService = new ValidateMapAdapter();
    }

    /**
     * Test method to check if the map is a connected graph.
     *
     * @throws UserCoreLogicException if there's an error in the UserCoreLogic
     * @throws URISyntaxException    if there's an error in URI syntax
     */
    @Test(expected = InvalidMapException.class)
    public void testMapIsConnectedGraph() throws UserCoreLogicException, URISyntaxException {
        
        d_testFilePath = getClass().getClassLoader().getResource("test_map_files/test_map_connectedGraph.map");

        assertNotNull(d_testFilePath);
        String l_url = new URI(d_testFilePath.getPath()).getPath();
        d_editMapService.handleLoadMap(l_url);

        d_validateMapService.execute(null);
    }

    /**
     * Test method to check if the continent forms a connected sub-graph.
     *
     * @throws UserCoreLogicException if there's an error in the UserCoreLogic
     * @throws URISyntaxException    if there's an error in URI syntax
     */    
    @Test(expected = InvalidMapException.class)
    public void testContinentConnectedSubGraph() throws UserCoreLogicException, URISyntaxException {
        
        d_testFilePath = getClass().getClassLoader().getResource("test_map_files/test_continent_subgraph.map");

        assertNotNull(d_testFilePath);
        String l_url = new URI(d_testFilePath.getPath()).getPath();
        d_editMapService.handleLoadMap(l_url);

        d_validateMapService.execute(null);
    }

    /**
     * Test method to validate the map service.
     *
     * @throws UserCoreLogicException if there's an error in the UserCoreLogic
     * @throws URISyntaxException    if there's an error in URI syntax
     */
    @Test
    public void testValidateMapService() throws UserCoreLogicException, URISyntaxException {
        
        d_testFilePath = getClass().getClassLoader().getResource("map_files/solar.map");

        assertNotNull(d_testFilePath);
        String l_url = new URI(d_testFilePath.getPath()).getPath();
        d_editMapService.handleLoadMap(l_url);

        String l_actualValue = d_validateMapService.execute(null);
        assertEquals(l_actualValue, "Map validation passed successfully!");
    }
}
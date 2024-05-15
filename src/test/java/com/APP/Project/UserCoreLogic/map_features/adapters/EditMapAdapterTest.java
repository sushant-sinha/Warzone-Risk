package com.APP.Project.UserCoreLogic.map_features.adapters;

import com.APP.Project.Main;
import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.exceptions.AbsentTagException;
import com.APP.Project.UserCoreLogic.gamePlay.GamePlayEngine;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URL;

/**
 * This class represents a test suite for the EditMapAdapter class.
 * It contains unit tests to verify the functionality of the EditMapAdapter class.
 *
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class EditMapAdapterTest {

    private static URL d_testCorruptedFilePath;
    private static URL d_testCorrectFilePath;
    private static GamePlayEngine d_GamePlayEngine;
    private EditConquestMapService d_editConquestMapService;

    /**
     * Setup tasks to be performed before the test class starts executing.
     * Initializes the application and sets up necessary objects.
     */
    @BeforeClass
    public static void beforeClass() {
        Main l_application = new Main();
        l_application.handleApplicationStartup();
        
        UserCoreLogic.getInstance().initialise();
        d_GamePlayEngine = UserCoreLogic.getGameEngine().getGamePlayEngine();
        d_testCorruptedFilePath = EditMapAdapterTest.class.getClassLoader().getResource("test_map_files/test_blank_field_in_conquest_map.map");
        d_testCorrectFilePath = EditMapAdapterTest.class.getClassLoader().getResource("map_files/conquest1.map");
    }

    /**
     * Setup tasks to be performed before each test method.
     * Initializes EditConquestMapService and GameEngine.
     */
    @Before
    public void before() {
        d_editConquestMapService = new EditConquestMapService();
        d_GamePlayEngine.initialise();
    }

    /**
     * Test case to verify the behavior when loading a corrupted map file.
     * Expects an AbsentTagException to be thrown.
     *
     * @throws Exception if an error occurs during the test execution.
     */
    @Test(expected = AbsentTagException.class)
    public void testLoadCorruptedMap() throws Exception {
        
        String l_url = new URI(d_testCorruptedFilePath.getPath()).getPath();
        d_editConquestMapService.loadConquestMap(l_url);
    }

    /**
     * Test case to verify the behavior when loading a correct map file.
     * Expects no exception to be thrown.
     *
     * @throws Exception if an error occurs during the test execution.
     */
    @Test(expected = Test.None.class)
    public void testLoadCorrectMapFile() throws Exception {
        
        String l_url = new URI(d_testCorrectFilePath.getPath()).getPath();
        d_editConquestMapService.loadConquestMap(l_url);
    }
}
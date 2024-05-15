package com.APP.Project.UserCoreLogic.map_features.adapters;

import com.APP.Project.Main;
import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.exceptions.AbsentTagException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URL;

/**
 * This class contains unit tests for the LoadMapAdapter class.
 *
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class LoadMapAdapterTest {
    private EditMapService d_editMapService;
    private static URL d_testCorruptedFilePath;
    private static URL d_testCorrectFilePath;

    /**
     * Performs setup operations before the test class runs.
     */    
    @BeforeClass
    public static void beforeClass() {
        Main l_application = new Main();
        l_application.handleApplicationStartup();
        UserCoreLogic.getGameEngine().initialise();
        d_testCorruptedFilePath = LoadMapAdapterTest.class.getClassLoader().getResource("test_map_files/test_blank_data_fields.map");
        d_testCorrectFilePath = LoadMapAdapterTest.class.getClassLoader().getResource("map_files/solar.map");
    }

    /**
     * Performs setup operations before each test method runs.
     */
    @Before
    public void before() {
        d_editMapService = new EditMapService();
        UserCoreLogic.getGameEngine().getMapEditorEngine().initialise();
    }

    /**
     * Tests loading a corrupted map file.
     *
     * @throws Exception If an exception occurs during the test.
     */
    @Test(expected = AbsentTagException.class)
    public void testLoadCorruptedMap() throws Exception {
        
        String l_url = new URI(d_testCorruptedFilePath.getPath()).getPath();
        d_editMapService.handleLoadMap(l_url);
    }

    /**
     * Tests loading a correct map file.
     *
     * @throws Exception If an exception occurs during the test.
     */
    @Test(expected = Test.None.class)
    public void testLoadCorrectMapFile() throws Exception {
        
        String l_url = new URI(d_testCorrectFilePath.getPath()).getPath();
        d_editMapService.handleLoadMap(l_url);
    }
}

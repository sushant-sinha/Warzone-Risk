package com.APP.Project.UserCoreLogic.map_features.adapters;

import com.APP.Project.Main;
import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.exceptions.EntityNotFoundException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidInputException;
import com.APP.Project.UserCoreLogic.exceptions.ResourceNotFoundException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;

/**
 * This class represents a test suite for the SaveMapAdapter class.
 *
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class SaveMapAdapterTest {
    private static Main d_application = new Main();
    private static ContinentAdapter d_ContinentService;
    private static CountryAdapter d_CountryService;
    private static CountryNeighborAdapter d_CountryNeighbourService;
    private static SaveMapService d_SaveMapService;
    private String testFile = "testing_save_file.map";

    // Temporary folder rule for file operations
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    /**
     * Setup method executed before the test class.
     */
    @BeforeClass
    public static void before() {
        d_application.handleApplicationStartup();
        UserCoreLogic.getInstance().initialise();

        d_ContinentService = new ContinentAdapter();
        d_CountryService = new CountryAdapter();
        d_CountryNeighbourService = new CountryNeighborAdapter();
        d_SaveMapService = new SaveMapService();
    }

    /**
     * Setup method executed before each test case.
     */
    @Before
    public void beforeTestCase() {
        UserCoreLogic.getGameEngine().getMapEditorEngine().initialise();
    }

    /**
     * Add content to the map file before running the test case.
     *
     * @throws InvalidInputException   If the input is invalid.
     * @throws EntityNotFoundException If the entity is not found.
     */    
    @Before
    public void addContentToTheMapFile() throws InvalidInputException, EntityNotFoundException {
        d_ContinentService.add("Asia", "10");
        d_ContinentService.add("Australia", "15");
        d_CountryService.add("Delhi", "Asia");
        d_CountryService.add("Mumbai", "Asia");
        d_CountryService.add("Melbourne", "Australia");
        d_CountryNeighbourService.add("Delhi", "Mumbai");
        d_CountryNeighbourService.add("Mumbai", "Delhi");
        d_CountryNeighbourService.add("Melbourne", "Delhi");
    }

    /**
     * Test saving the file.
     *
     * @throws ResourceNotFoundException If the resource is not found.
     * @throws InvalidInputException     If the input is invalid.
     * @throws IOException               If an IO error occurs.
     */
    @Test(expected = Test.None.class)
    public void testSaveFile() throws ResourceNotFoundException, InvalidInputException, IOException {
        
        final File testFileObject = tempFolder.newFile(testFile);
        String response = d_SaveMapService.saveToFile(testFileObject);

        assert testFileObject.exists();
        assertNotNull(response);
    }

}

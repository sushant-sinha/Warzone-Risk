package com.APP.Project.UserCoreLogic.map_features.adapters;

import com.APP.Project.Main;
import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.exceptions.EntityNotFoundException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidInputException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the ContinentAdapter class.
 *
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class ContinentAdapterTest {
    private static Main d_Application = new Main();
    private static ContinentAdapter d_ContinentService;

    /**
     * Performs setup operations before the test class is executed.
     */
    @BeforeClass
    public static void beforeClass() {
        d_Application.handleApplicationStartup();
    }

    /**
     * Performs setup operations before each test case.
     */
    @Before
    public void beforeTestCase() {
        UserCoreLogic.getInstance().initialise();
        d_ContinentService = new ContinentAdapter();
    }

    /**
     * Tests the behavior when an invalid continent value is provided.
     *
     * @throws InvalidInputException if the input is invalid.
     */    
    @Test(expected = InvalidInputException.class)
    public void testWrongContinentValue() throws InvalidInputException {
        d_ContinentService.add("Asia", "StringValue");
    }

    /**
     * Tests the addition and removal of a continent.
     *
     * @throws EntityNotFoundException if the entity is not found.
     * @throws InvalidInputException   if the input is invalid.
     */
    @Test(expected = Test.None.class)
    public void testAddAndRemoveContinent() throws EntityNotFoundException, InvalidInputException {
        String l_responseOfAddOp = d_ContinentService.add("Asia", "10");
        assertNotNull(l_responseOfAddOp);

        String l_responseOfRemoveOp = d_ContinentService.remove("Asia");
        assertNotNull(l_responseOfRemoveOp);
    }
}
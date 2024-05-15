package com.APP.Project.UserCoreLogic.gamePlay.services;

import com.APP.Project.Main;
import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.constants.enums.StrategyType;
import com.APP.Project.UserCoreLogic.exceptions.InvalidInputException;
import com.APP.Project.UserCoreLogic.exceptions.UserCoreLogicException;
import com.APP.Project.UserCoreLogic.gamePlay.GamePlayEngine;
import com.APP.Project.UserCoreLogic.map_features.adapters.EditMapService;
import com.APP.Project.UserCoreLogic.game_entities.Player;
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
 * This class represents the unit test for the ShowMapService class.
 * It tests various functionalities related to displaying player content on the map.
 *
 * @author Rupal Kapoor
 * @version 1.0
 */
public class CountryDistributionServiceTest {
    private static Main d_Application;
    private static URL d_TestFilePath;
    private CountryDistributionService d_distributeCountriesService;
    private static GamePlayEngine d_GamePlayEngine;

    /**
     * This method runs before the test case class runs.
     * It additionally initializes different objects required to perform test.
     *
     * @throws IOException        If exception while coping the predefined files.
     * @throws URISyntaxException If the path to parent directory of the files doesn't exist.
     */
    @BeforeClass
    public static void createPlayersList() throws IOException, URISyntaxException {
        d_Application = new Main();
        d_Application.handleApplicationStartup();

        // (Re)initialise the VM.
        UserCoreLogic.getInstance().initialise();
        d_GamePlayEngine = UserCoreLogic.getGameEngine().getGamePlayEngine();

        d_Application.restoreMapFiles();
        d_TestFilePath = CountryDistributionServiceTest.class.getClassLoader().getResource("test_map_files/test_map.map");
    }

    /**
     * This method is used for setting up the required objects before performing test.
     *
     * @throws UserCoreLogicException is thrown when exception is generated during execution.
     * @throws URISyntaxException is thrown in case the error while parsing the string representing the path.
     */
    @Before
    public void before() throws UserCoreLogicException, URISyntaxException {
        d_GamePlayEngine.initialise();
        // Loads the map
        EditMapService l_editMapService = new EditMapService();
        assertNotNull(d_TestFilePath);
        String l_url = new URI(d_TestFilePath.getPath()).getPath();
        l_editMapService.handleLoadMap(l_url);

        Player l_player1 = new Player("User_1", StrategyType.HUMAN);
        Player l_player2 = new Player("User_2", StrategyType.HUMAN);

        d_GamePlayEngine.addPlayer(l_player1);
        d_GamePlayEngine.addPlayer(l_player2);

        d_distributeCountriesService = new CountryDistributionService();
    }

    /**
     * This method tests whether the player list is empty or not. Passes if list contains players objects, otherwise fails.
     *
     * @throws InvalidInputException This exception is thrown if invalid player count.
     */
    @Test(expected = Test.None.class)
    public void testNumberOfPlayer() throws InvalidInputException {
        d_distributeCountriesService.distributeCountries();
    }

    /**
     * This method tests whether the count of countries required to be assigned to the player is correct or not.
     *
     * @throws InvalidInputException This exception is thrown if player objects list is empty.
     */
    @Test(expected = Test.None.class)
    public void testPlayerCountryCount() throws InvalidInputException {
        d_distributeCountriesService.distributeCountries();
        assertEquals(5, d_GamePlayEngine.getPlayerList().get(0).getAssignedCountryCount());
    }

    /**
     * This method tests whether the countries are correctly assigned or not.
     *
     * @throws InvalidInputException Throws if player objects list is empty.
     */
    @Test(expected = Test.None.class)
    public void testAssignedCountriesCount() throws InvalidInputException {
        String l_response = d_distributeCountriesService.distributeCountries();
        assertNotNull(l_response);

        int l_size = d_GamePlayEngine.getPlayerList().get(0).getAssignedCountries().size();
        assertEquals(5, l_size);
    }
}
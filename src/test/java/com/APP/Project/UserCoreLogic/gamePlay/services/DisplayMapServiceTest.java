package com.APP.Project.UserCoreLogic.gamePlay.services;

import com.APP.Project.Main;
import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.constants.enums.StrategyType;
import com.APP.Project.UserCoreLogic.game_entities.Player;
import com.APP.Project.UserCoreLogic.exceptions.*;
import com.APP.Project.UserCoreLogic.map_features.adapters.EditMapService;
import com.jakewharton.fliptables.FlipTable;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This class represents the unit test for the ShowMapService class.
 * It tests various functionalities related to displaying player content on the map.
 *
 * @author Rupal Kapoor
 */
public class DisplayMapServiceTest {
    private DisplayMapService d_showMapService;
    private List<Player> d_playerList;
    private static URL d_TestFilePath;

    /**
     * This method executes once before all test methods in the class.
     * It initializes necessary components for testing.
     *
     */
    @BeforeClass
    public static void beforeClass() {
        Main l_application = new Main();
        l_application.handleApplicationStartup();

        d_TestFilePath = DisplayMapServiceTest.class.getClassLoader().getResource("test_map_files/test_map.map");
    }

    /**
     * This method executes before each test method.
     * It sets up the environment required for individual test cases.
     *
     * @throws InvalidInputException      If the input is invalid.
     * @throws EntityNotFoundException   If the entity is not found.
     * @throws URISyntaxException        If the URI is invalid.
     * @throws InvalidMapException       If the map is invalid.
     * @throws ResourceNotFoundException If the resource is not found.
     * @throws AbsentTagException        If a tag is absent.
     */
    @Before
    public void before() throws InvalidInputException, EntityNotFoundException, URISyntaxException, InvalidMapException, ResourceNotFoundException, AbsentTagException {
        // (Re)initialise the VM.
        UserCoreLogic.getInstance().initialise();

        EditMapService l_editMapService = new EditMapService();
        d_showMapService = new DisplayMapService();
        assertNotNull(d_TestFilePath);
        String l_url = new URI(d_TestFilePath.getPath()).getPath();
        l_editMapService.handleLoadMap(l_url);

        CountryDistributionService l_distributeCountriesService = new CountryDistributionService();
        PlayerService l_playerService = new PlayerService();
        l_playerService.add("User_1", StrategyType.HUMAN.getJsonValue());
        l_playerService.add("User_2", StrategyType.HUMAN.getJsonValue());

        l_distributeCountriesService.distributeCountries();
        d_playerList = UserCoreLogic.getGameEngine().getGamePlayEngine().getPlayerList();
    }

    /**
     * This method is used to test the functionality to display player content on the map.
     */
    @Test
    public void testShowPlayerContent() {
        String[] l_header1 = {"USER_1", "Mercury-South", "Mercury-East", "Mercury-West", "Mercury-North", "Venus-South"};
        String[] l_playerContent1 = {"Army Count", "0", "0", "0", "0", "0"};

        String l_PlayerExpectedData = FlipTable.of(l_header1, new String[][]{l_playerContent1});
        String l_playerActualData = d_showMapService.showPlayerContent(d_playerList.get(0));
        assertEquals(l_PlayerExpectedData, l_playerActualData);
    }
}
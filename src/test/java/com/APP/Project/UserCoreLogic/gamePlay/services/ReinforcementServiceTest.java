package com.APP.Project.UserCoreLogic.gamePlay.services;

import com.APP.Project.Main;
import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.constants.enums.StrategyType;
import com.APP.Project.UserCoreLogic.exceptions.*;
import com.APP.Project.UserCoreLogic.gamePlay.GamePlayEngine;
import com.APP.Project.UserCoreLogic.map_features.MapEditorEngine;
import com.APP.Project.UserCoreLogic.map_features.adapters.EditMapService;
import com.APP.Project.UserCoreLogic.game_entities.Player;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This file contains the ReinforcementServiceTest class which tests the functionality
 * of the ReinforcementService class.
 *
 * @author Rupal Kapoor
 */
public class ReinforcementServiceTest {
    private static Main d_Application = new Main();
    private static MapEditorEngine d_MapEditorEngine;
    private static EditMapService d_EditMapService;
    private static URL d_TestFile;
    private static CountryDistributionService d_DistributeCountriesService;
    private static ReinforcementService d_AssignReinforcementService;
    private static GamePlayEngine d_GamePlayEngine;


    /**
     * Initializes different objects required to perform test.
     */
    @BeforeClass
    public static void beforeClass() {
        d_Application.handleApplicationStartup();
        UserCoreLogic.getInstance().initialise();
        d_GamePlayEngine = UserCoreLogic.getGameEngine().getGamePlayEngine();
        d_MapEditorEngine = UserCoreLogic.getGameEngine().getMapEditorEngine();
        d_TestFile = ReinforcementServiceTest.class.getClassLoader().getResource("test_map_files/test_map.map");
    }


    /**
     * Setting up the required Objects before test run.
     *
     * @throws InvalidInputException     Throws if provided argument and its value(s) are not valid.
     * @throws AbsentTagException        Throws if tag is absent in .map file.
     * @throws InvalidMapException       Throws if map file is invalid.
     * @throws ResourceNotFoundException Throws if file not found.
     * @throws EntityNotFoundException   Throws if entity not found while searching.
     * @throws URISyntaxException        If error while parsing the string representing the path.
     */
    @Before
    public void before() throws InvalidInputException, AbsentTagException, InvalidMapException, ResourceNotFoundException, EntityNotFoundException, URISyntaxException {
        d_GamePlayEngine.initialise();
        d_MapEditorEngine.initialise();
        d_MapEditorEngine.getCountryList();
        d_AssignReinforcementService = new ReinforcementService();

        Player l_player1 = new Player("USER_1", StrategyType.HUMAN);
        Player l_player2 = new Player("USER_1", StrategyType.HUMAN);

        d_GamePlayEngine.addPlayer(l_player1);
        d_GamePlayEngine.addPlayer(l_player2);

        d_EditMapService = new EditMapService();
        assertNotNull(d_TestFile);
        String l_url = new URI(d_TestFile.getPath()).getPath();
        d_EditMapService.handleLoadMap(l_url);
        d_DistributeCountriesService = new CountryDistributionService();
        d_DistributeCountriesService.distributeCountries();
    }

    /**
     * This test will test the assign country list. It checks whether the assign country list is empty or not.
     */
    @Test(expected = Test.None.class)
    public void testAssignCountry() {
        for (Player l_player : d_GamePlayEngine.getPlayerList()) {
            assertNotNull(l_player.getAssignedCountries());
        }
    }

    /**
     * This Test will test the re-calculate reinforced army. It checks whether the army is reinforced properly or not.
     *
     * @throws EntityNotFoundException Throws if entity not found while searching.
     */
    @Test
    public void testingCalculatedReinforcedArmyValue() throws EntityNotFoundException {
        d_AssignReinforcementService.execute();
        int l_reinforcementArmies = d_GamePlayEngine.getPlayerList().get(0).getReinforcementCount();
        assertEquals(9, l_reinforcementArmies);

        int l_reinforcementArmies1 = d_GamePlayEngine.getPlayerList().get(1).getReinforcementCount();
        assertEquals(13, l_reinforcementArmies1);
    }
}

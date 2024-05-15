package com.APP.Project.UserCoreLogic.gamePlay.services;

import com.APP.Project.Main;
import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.common.services.SaveGameService;
import com.APP.Project.UserCoreLogic.constants.enums.StrategyType;
import com.APP.Project.UserCoreLogic.exceptions.*;
import com.APP.Project.UserCoreLogic.gamePlay.GamePlayEngine;
import com.APP.Project.UserCoreLogic.map_features.adapters.EditMapService;
import com.APP.Project.UserCoreLogic.phases.IssueOrder;
import com.APP.Project.UserCoreLogic.game_entities.Player;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests whether the SaveGameService is capable of storing the runtime information inside the file properly
 * or not.
 *
 * @author Rupal Kapoor
 * @version 1.0
 */
public class SaveGameServiceTest {
    private static Main d_application;
    private static URL d_testFilePath;
    private static URL d_testSavedFilePath;
    private static GamePlayEngine d_gamePlayEngine;
    private CountryDistributionService d_distributeCountriesService;
    private ReinforcementService d_assignReinforcementService;

    /**
     * This is responsible for creating a temporary folder for test case.
     */
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();


    /**
     * Runs before the test case runs.
     * It also initializes different objects required to perform test.
     */
    @BeforeClass
    public static void beforeClass() {
        d_application = new Main();
        d_application.handleApplicationStartup();
        UserCoreLogic.getInstance().initialise();

        d_gamePlayEngine = UserCoreLogic.getGameEngine().getGamePlayEngine();
        d_testFilePath = SaveGameServiceTest.class.getClassLoader().getResource("test_map_files/test_earth.map");
        d_testSavedFilePath = SaveGameServiceTest.class.getClassLoader().getResource("test_game_files/test_earth.warzone");
    }

    /**
     * This method is responsible for setting up the required objects before performing test.
     *
     * @throws AbsentTagException        is thrown in case any tag is missing in the json file.
     * @throws InvalidMapException       is thrown in case map file is invalid.
     * @throws ResourceNotFoundException is thrown in case the file not found.
     * @throws InvalidInputException     is thrown in case user input is invalid.
     * @throws EntityNotFoundException   is thrown in case entity not found.
     * @throws URISyntaxException        is thrown in case URI syntax problem.
     */
    @Before
    public void before() throws AbsentTagException, InvalidMapException, ResourceNotFoundException, InvalidInputException, EntityNotFoundException, URISyntaxException {
        d_gamePlayEngine.initialise();

        EditMapService l_editMapService = new EditMapService();
        assertNotNull(d_testFilePath);

        String l_url = new URI(d_testFilePath.getPath()).getPath();
        l_editMapService.handleLoadMap(l_url);

        Player l_player1 = new Player("player_1", StrategyType.HUMAN);
        Player l_player2 = new Player("player_2", StrategyType.HUMAN);

        d_gamePlayEngine.addPlayer(l_player1);
        d_gamePlayEngine.addPlayer(l_player2);

        d_distributeCountriesService = new CountryDistributionService();
        // Distributes countries between players.
        d_distributeCountriesService.distributeCountries();

        d_assignReinforcementService = new ReinforcementService();
        d_assignReinforcementService.execute();

        UserCoreLogic.getGameEngine().setGamePhase(new IssueOrder(UserCoreLogic.getGameEngine()));
    }

    /**
     * This method is responsible for testing if the content being saved into the provided file or not.
     *
     * @throws IOException is thrown in case any error occurred while saving the engines to file.
     */
    @Test(expected = Test.None.class)
    public void testSaveFile() throws IOException {
        SaveGameService l_saveGameService = new SaveGameService();
        l_saveGameService.toJSON();
        // This is the actual JSONObject.
        JSONObject l_actualJSONObject = l_saveGameService.getGameEngineJSONData();
        StringBuilder l_fileContentBuilder = new StringBuilder();
        try (BufferedReader  br = new BufferedReader(new FileReader(d_testSavedFilePath.getPath()))) {

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                l_fileContentBuilder.append(sCurrentLine);
            }
        }
        // Load the string content in JSONObject. This is expected JSONObject.
        JSONObject l_savedJSONObject = new JSONObject(l_fileContentBuilder.toString());
        // Check if the both JSONObject are having the same content.
        boolean isEqual = l_actualJSONObject.similar(l_savedJSONObject);
        assertTrue(isEqual);
    }
}
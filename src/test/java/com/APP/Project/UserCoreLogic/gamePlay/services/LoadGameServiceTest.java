package com.APP.Project.UserCoreLogic.gamePlay.services;

import com.APP.Project.Main;
import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.common.services.LoadGameService;
import com.APP.Project.UserCoreLogic.game_entities.Player;
import com.APP.Project.UserCoreLogic.exceptions.*;
import com.APP.Project.UserCoreLogic.gamePlay.GamePlayEngine;
import com.APP.Project.UserCoreLogic.map_features.MapEditorEngine;
import com.APP.Project.UserCoreLogic.map_features.adapters.EditMapService;
import com.APP.Project.UserCoreLogic.Container.PlayerContainer;
import com.APP.Project.UserCoreLogic.Utility.FileValidationUtil;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * This game tests if the file has been loaded successfully.
 *
 * @author Rupal Kapoor
 */
public class LoadGameServiceTest {
    private static Main d_application;
    private static URL d_testFilePath;
    private static URL d_testSavedFilePath;
    private static GamePlayEngine d_gamePlayEngine;
    private final PlayerContainer d_PLAYER_REPOSITORY = new PlayerContainer();

    /**
     * This method runs before the test case class runs.
     * It also initializes different objects required to perform the test.
     */
    @BeforeClass
    public static void beforeClass() {
        d_application = new Main();
        d_application.handleApplicationStartup();
        UserCoreLogic.getInstance().initialise();

        d_gamePlayEngine = UserCoreLogic.getGameEngine().getGamePlayEngine();
        d_testFilePath = LoadGameServiceTest.class.getClassLoader().getResource("test_map_files/test_earth.map");
        d_testSavedFilePath = LoadGameServiceTest.class.getClassLoader().getResource("test_game_files/test_earth.warzone");
    }

    /**
     * This method sets the context of the test case for loading the game.
     *
     * @throws InvalidMapException       Throws if the map was not valid.
     * @throws ResourceNotFoundException Throws if file not found.
     * @throws InvalidInputException     Throws if the user command is invalid.
     * @throws AbsentTagException        Throws if any tag is missing in map file.
     * @throws EntityNotFoundException   Throws if entity is missing.
     * @throws URISyntaxException        If any path to file is not valid.
     */
    @Before
    public void before() throws InvalidInputException,
            EntityNotFoundException,
            InvalidMapException,
            ResourceNotFoundException,
            AbsentTagException,
            URISyntaxException {
        d_gamePlayEngine.initialise();

        EditMapService l_editMapService = new EditMapService();
        assertNotNull(d_testFilePath);

        String l_url = new URI(d_testFilePath.getPath()).getPath();
        l_editMapService.handleLoadMap(l_url);
    }

    /**
     * This method tests the loading of a game file.
     * This test case entials the testing of the information loaded into the game engines are valid or
     * not.
     *
     * @throws IOException Is thrown in case any exception while reading the file.
     * @throws UserCoreLogicException Is thrown in case any error while loading the game.
     */
    @Test(expected = Test.None.class)
    public void testLoadFile() throws UserCoreLogicException, IOException {
        LoadGameService l_loadGameService = new LoadGameService();
        File l_targetFile = FileValidationUtil.retrieveGameFile(d_testSavedFilePath.getPath());
        StringBuilder l_fileContentBuilder = new StringBuilder();
        try (BufferedReader l_bufferedReader = new BufferedReader(new FileReader(l_targetFile))) {
            String l_currentLine;
            while ((l_currentLine = l_bufferedReader.readLine()) != null) {
                l_fileContentBuilder.append(l_currentLine);
            }
        }
        JSONObject jsonObject = new JSONObject(l_fileContentBuilder.toString());
        l_loadGameService.loadGameState(jsonObject);
        assertEquals(GamePlayEngine.getCurrentExecutionIndex(), 0);

        MapEditorEngine l_mapEditorEngine = UserCoreLogic.getGameEngine().getMapEditorEngine();
        assertEquals(l_mapEditorEngine.getContinentList().size(), 2);

        Player l_player1 = d_PLAYER_REPOSITORY.findByPlayerName("player_1");
        assertEquals(l_player1.getAssignedCountries().size(), 4);
    }
}

package com.APP.Project.UserCoreLogic;

import com.APP.Project.UserCoreLogic.constants.enums.StrategyType;
import com.APP.Project.Main;
import com.APP.Project.UserCoreLogic.game_entities.Player;
import com.APP.Project.UserCoreLogic.exceptions.UserCoreLogicException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class tests tournament engine.
 *
 * @author Bhoomiben Bhatt
 * @version 1.0
 */
public class TournamentEngineTest {
    private static final Main d_Application = new Main();
    private static final List<String> d_mapFiles = new ArrayList<>();
    private TournamentEngine d_tournamentEngine;

    /**
     * Executes before the test class to set up necessary objects for testing.
     *
     * @throws URISyntaxException If a file path is invalid.
     */
    @BeforeClass
    public static void beforeClass() throws URISyntaxException {
        d_Application.handleApplicationStartup();
        d_mapFiles.add("test_earth.map");
        d_mapFiles.add("test_craters.map");
    }

    /**
     * Re-initializes the continent list before test case run.
     */
    @Before
    public void beforeTestCase() {
        // (Re)initialise Virtual Machine.
        UserCoreLogic.getInstance().initialise();

        // Setup TournamentEngine.
        d_tournamentEngine = UserCoreLogic.TOURNAMENT_ENGINE();
        d_tournamentEngine.setMapFileList(d_mapFiles);
        d_tournamentEngine.setMaxNumberOfTurns(3);
        d_tournamentEngine.setNumberOfGames(20);
        d_tournamentEngine.addPlayer(new Player("Player_1", StrategyType.AGGRESSIVE));
        d_tournamentEngine.addPlayer(new Player("Player_2", StrategyType.BENEVOLENT));
    }

    /**
     * Verifies that the tournament cycles through the specified map files and turn numbers, yielding results without exceptions.
     *
     * @throws UserCoreLogicException If an exception occurs during tournament execution..
     */
    @Test(expected = Test.None.class)
    public void testTournament() throws UserCoreLogicException {
        d_tournamentEngine.onStart(false);
    }
}

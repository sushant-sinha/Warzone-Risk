package com.APP.Project.UserCoreLogic.game_entities.orders;

import com.APP.Project.Main;
import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.constants.enums.StrategyType;
import com.APP.Project.UserCoreLogic.exceptions.*;
import com.APP.Project.UserCoreLogic.gamePlay.GamePlayEngine;
import com.APP.Project.UserCoreLogic.map_features.adapters.EditMapService;
import com.APP.Project.UserCoreLogic.game_entities.Player;
import com.APP.Project.UserCoreLogic.game_entities.cards.DiplomacyCard;
import com.APP.Project.UserCoreLogic.gamePlay.services.CountryDistributionService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * This class is for testing the Negotiate card operation.
 *
 * @author Sushant Sinha
 */
public class NegotiateOrderTest {
    private static Main d_Application;
    private static URL d_TestFilePath;
    private static GamePlayEngine d_GamePlayEngine;
    private CountryDistributionService d_distributeCountriesService;
    private List<Player> d_playerList;

    /**
     * Runs before the test case class runs; Initializes different objects required to perform test.
     */
    @BeforeClass
    public static void createPlayersList() {
        d_Application = new Main();
        d_Application.handleApplicationStartup();
        UserCoreLogic.getInstance().initialise();

        d_GamePlayEngine = UserCoreLogic.getGameEngine().getGamePlayEngine();
        d_TestFilePath = BombOrderTest.class.getClassLoader().getResource("map_files/solar.map");
    }

    /**
     * Setting up the required objects before performing test.
     *
     * @throws AbsentTagException        Throws if any tag is missing in the map file.
     * @throws InvalidMapException       Throws if the map is invalid.
     * @throws ResourceNotFoundException Throws if resource is not available
     * @throws InvalidInputException     Throws if user input is invalid.
     * @throws EntityNotFoundException   Throws if entity not found.
     * @throws URISyntaxException        Throws if URI syntax problem.
     */
    @Before
    public void setup() throws AbsentTagException, InvalidMapException, ResourceNotFoundException, InvalidInputException, EntityNotFoundException, URISyntaxException {
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
        d_playerList = d_GamePlayEngine.getPlayerList();
        d_distributeCountriesService = new CountryDistributionService();
        d_distributeCountriesService.distributeCountries();
    }

    /**
     * Checks that player is successfully added to the FriendPlayer List.
     *
     * @throws EntityNotFoundException Throws if would not able to find players.
     * @throws CardNotFoundException   Throws if not able to find cards.
     */
    @Test
    public void testExecute() throws EntityNotFoundException, CardNotFoundException {
        Player l_player1 = d_playerList.get(0);
        Player l_player2 = d_playerList.get(1);
        l_player1.addCard(new DiplomacyCard());
        NegotiateOrder negotiateOrder = new NegotiateOrder(l_player1, "User_2");
        negotiateOrder.execute();
        assertTrue(l_player1.getFriendPlayers().contains(l_player2));
        assertTrue(l_player2.getFriendPlayers().contains(l_player1));
    }
}
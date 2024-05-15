package com.APP.Project.UserCoreLogic.game_entities.orders;

import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.constants.enums.StrategyType;
import com.APP.Project.UserCoreLogic.exceptions.*;
import com.APP.Project.UserCoreLogic.gamePlay.GamePlayEngine;
import com.APP.Project.UserCoreLogic.map_features.adapters.EditMapService;
import com.APP.Project.Main;
import com.APP.Project.UserCoreLogic.game_entities.Country;
import com.APP.Project.UserCoreLogic.game_entities.Player;
import com.APP.Project.UserCoreLogic.game_entities.cards.BlockadeCard;
import com.APP.Project.UserCoreLogic.game_entities.cards.BombCard;
import com.APP.Project.UserCoreLogic.gamePlay.services.CountryDistributionService;
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
 * This class is for testing various operations performed during the execution of the bomb command.
 *
 * @author Sushant Sinha
 */
public class BombOrderTest {
    private static Main d_Application;
    private static URL d_TestFilePath;
    private static GamePlayEngine d_GamePlayEngine;
    private CountryDistributionService d_distributeCountriesService;
    private List<Player> d_playerList;

    /**
     * Runs before the test case class runs; Initializes different objects required to perform test.
     */
    @BeforeClass
    public static void beforeClass() {
        d_Application = new Main();
        d_Application.handleApplicationStartup();
        // (Re)initialise the VM.
        UserCoreLogic.getInstance().initialise();

        d_GamePlayEngine = UserCoreLogic.getGameEngine().getGamePlayEngine();
        d_TestFilePath = BombOrderTest.class.getClassLoader().getResource("test_map_files/test_map.map");
    }

    /**
     * Setting up the required objects before performing test.
     *
     * @throws AbsentTagException        Throws if any tag is missing in the map file.
     * @throws InvalidMapException       Throws if map file is invalid.
     * @throws ResourceNotFoundException Throws if the file not found.
     * @throws InvalidInputException     Throws if user input is invalid.
     * @throws EntityNotFoundException   Throws if entity not found.
     * @throws URISyntaxException        Throws if URI syntax problem.o
     */
    @Before
    public void before() throws AbsentTagException, InvalidMapException, ResourceNotFoundException, InvalidInputException, EntityNotFoundException, URISyntaxException {
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
        //Distributes countries between players.
        d_distributeCountriesService.distributeCountries();
    }

    /**
     * For testing: the bomb operation for the player having bomb card.
     *
     * @throws EntityNotFoundException Throws if entity not found.
     * @throws InvalidOrderException   Throws if exception while executing the order.
     * @throws CardNotFoundException   Card doesn't found in the player's card list.
     */
    @Test(expected = Test.None.class)
    public void testBombOperationWithBombCard()
            throws EntityNotFoundException, InvalidOrderException, CardNotFoundException {
        Player l_player1 = d_playerList.get(0);
        Player l_player2 = d_playerList.get(1);
        List<Country> l_player2AssignCountries = l_player2.getAssignedCountries();

        //Assigning bomb card manually
        l_player1.addCard(new BombCard());
        BombOrder l_bombOrder = new BombOrder(l_player2AssignCountries.get(0).getCountryName(), l_player1);
        l_player2AssignCountries.get(0).setNumberOfArmies(10);
        int l_armies = l_player2AssignCountries.get(0).getNumberOfArmies();
        l_bombOrder.execute();
        assertEquals(l_player2AssignCountries.get(0).getNumberOfArmies(), l_armies / 2);
    }

    /**
     * For testing: the bomb operation if player doesn't have bomb card.
     *
     * @throws EntityNotFoundException Throws if entity not found.
     * @throws InvalidOrderException   Throws if exception while executing the order.
     * @throws CardNotFoundException   Card doesn't found in the player's card list.
     */
    @Test(expected = CardNotFoundException.class)
    public void testBombOperationWithOutBombCard()
            throws EntityNotFoundException, InvalidOrderException, CardNotFoundException {
        Player l_player1 = d_playerList.get(0);
        Player l_player2 = d_playerList.get(1);
        List<Country> l_player2AssignCountries = l_player2.getAssignedCountries();

        //Assigning blockade card manually.
        l_player1.addCard(new BlockadeCard());
        BombOrder l_bombOrder = new BombOrder(l_player2AssignCountries.get(0).getCountryName(), l_player1);
        l_player2AssignCountries.get(0).setNumberOfArmies(10);
        int l_armies = l_player2AssignCountries.get(0).getNumberOfArmies();
        l_bombOrder.execute();
        assertEquals(l_player2AssignCountries.get(0).getNumberOfArmies(), l_armies / 2);
    }

    /**
     * For testing: the bomb operation when player performs bomb operation on its own country.
     *
     * @throws EntityNotFoundException Throws if entity not found.
     * @throws InvalidOrderException   Throws if exception while executing the order.
     * @throws CardNotFoundException   Card doesn't found in the player's card list.
     */
    @Test(expected = InvalidOrderException.class)
    public void testBombOperationOnPlayerOwnedCountry()
            throws EntityNotFoundException, InvalidOrderException, CardNotFoundException {
        Player l_player1 = d_playerList.get(0);
        List<Country> l_assignCountries = l_player1.getAssignedCountries();
        BombOrder l_bombOrder = new BombOrder(l_assignCountries.get(0).getCountryName(), l_player1);
        l_bombOrder.execute();
    }

    /**
     * For testing: whether the first execution has removed the card from the list of cards available to player.
     *
     * @throws EntityNotFoundException Throws if entity not found.
     * @throws InvalidOrderException   Throws if exception while executing the order.
     * @throws CardNotFoundException   Card doesn't found in the player's card list.
     */
    @Test(expected = CardNotFoundException.class)
    public void testCardSuccessfullyRemoved()
            throws EntityNotFoundException, InvalidOrderException, CardNotFoundException {
        Player l_player1 = d_playerList.get(0);
        Player l_player2 = d_playerList.get(1);
        List<Country> l_player2AssignCountries = l_player2.getAssignedCountries();

        //Assigning bomb card manually.
        l_player1.addCard(new BombCard());
        BombOrder l_bombOrder = new BombOrder(l_player2AssignCountries.get(0).getCountryName(), l_player1);
        l_bombOrder.execute();

        //Now during second execution player will not have a bomb card as we have assigned only one bomb card manually.
        //So it will raise InvalidCommandException.
        l_bombOrder.execute();
    }

    /**
     * For testing: that in case of negotiation bomb order will not execute.
     *
     * @throws InvalidOrderException   Throws if exception while executing the order.
     * @throws CardNotFoundException   Card doesn't found in the player's card list.
     * @throws EntityNotFoundException Throws if entity not found.
     */
    @Test(expected = Test.None.class)
    public void testNegotiate() throws InvalidOrderException, CardNotFoundException, EntityNotFoundException {
        Player l_player1 = d_playerList.get(0);
        Player l_player2 = d_playerList.get(1);
        l_player1.addNegotiatePlayer(l_player2);

        List<Country> l_player2AssignCountries = l_player2.getAssignedCountries();
        int l_expectedArmies = l_player2AssignCountries.get(0).getNumberOfArmies();
        l_player1.addCard(new BombCard());
        BombOrder l_bombOrder = new BombOrder(l_player2AssignCountries.get(0).getCountryName(), l_player1);
        l_bombOrder.execute();
        assertEquals(l_player2AssignCountries.get(0).getNumberOfArmies(), l_expectedArmies);
    }
}
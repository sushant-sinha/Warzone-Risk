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
 * This class is for testing various operations performed during the execution of the blockade command.
 *
 * @author Sushant Sinha
 */
public class BlockadeOrderTest {
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
        d_TestFilePath = BlockadeOrderTest.class.getClassLoader().getResource("test_map_files/test_map.map");
    }

    /**
     * Setting up the required objects before performing test.
     *
     * @throws AbsentTagException        Throws if any tag is missing in the map file.
     * @throws InvalidMapException       Throws if map file is invalid.
     * @throws ResourceNotFoundException Throws if the file not found.
     * @throws InvalidInputException     Throws if user input is invalid.
     * @throws EntityNotFoundException   Throws if entity not found.
     * @throws URISyntaxException        Throws if URI syntax problem.
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
        d_distributeCountriesService.distributeCountries();
    }

    /**
     * For testing: the blockade operation for the player having blockade card.
     *
     * @throws EntityNotFoundException Throws if entity not found.
     * @throws InvalidOrderException   Throws if exception while executing the order.
     * @throws CardNotFoundException   Card doesn't found in the player's card list.
     */
    @Test(expected = Test.None.class)
    public void testBlockadeOperationWithBlockadeCard()
            throws EntityNotFoundException, InvalidOrderException, CardNotFoundException {
        Player l_player1 = d_playerList.get(0);
        List<Country> l_player1AssignCountries = l_player1.getAssignedCountries();
        l_player1.addCard(new BlockadeCard());
        BlockadeOrder l_blockadeOrder = new BlockadeOrder(l_player1AssignCountries.get(0).getCountryName(), l_player1);
        Country l_country = l_player1AssignCountries.get(0);
        l_country.setNumberOfArmies(10);
        int l_armies = l_country.getNumberOfArmies();
        l_blockadeOrder.execute();
        assertEquals(l_country.getNumberOfArmies(), l_armies * 3);
    }

    /**
     * For testing: the blockade operation for the player not having blockade card.
     *
     * @throws EntityNotFoundException Throws if entity not found.
     * @throws InvalidOrderException   Throws if exception while executing the order.
     * @throws CardNotFoundException   Card doesn't found in the player's card list.
     */

    @Test(expected = CardNotFoundException.class)
    public void testBlockadeOperationWithOutBlockadeCard()
            throws EntityNotFoundException, InvalidOrderException, CardNotFoundException {
        Player l_player1 = d_playerList.get(0);
        List<Country> l_player1AssignCountries = l_player1.getAssignedCountries();
        l_player1.addCard(new BombCard());
        BlockadeOrder l_blockadeOrder = new BlockadeOrder(l_player1AssignCountries.get(0).getCountryName(), l_player1);
        Country l_country = l_player1AssignCountries.get(0);
        l_country.setNumberOfArmies(10);
        int l_armies = l_country.getNumberOfArmies();
        l_blockadeOrder.execute();
        assertEquals(l_country.getNumberOfArmies(), l_armies * 3);
    }

    /**
     * For testing: the blockade operation when player performs blockade operation on other player's country country.
     *
     * @throws EntityNotFoundException Throws if entity not found.
     * @throws InvalidOrderException   Throws if exception while executing the order.
     * @throws CardNotFoundException   Card doesn't found in the player's card list.
     */
    @Test(expected = InvalidOrderException.class)
    public void testBlockadeOperationOnOtherPlayerOwnedCountry()
            throws EntityNotFoundException, InvalidOrderException, CardNotFoundException {
        Player l_player1 = d_playerList.get(0);
        Player l_player2 = d_playerList.get(1);
        List<Country> l_player2AssignCountries = l_player2.getAssignedCountries();
        l_player1.addCard(new BlockadeCard());
        BlockadeOrder l_blockadeOrder = new BlockadeOrder(l_player2AssignCountries.get(0).getCountryName(), l_player1);
        l_blockadeOrder.execute();
    }

    /**
     * For testing: whether the first execution has removed the card from the list of cards available to player.
     *
     * @throws EntityNotFoundException Throws if entity not found.
     * @throws InvalidOrderException   Throws if exception while executing the order.
     * @throws CardNotFoundException   Card doesn't found in the player's card list.
     */
    @Test(expected = CardNotFoundException.class)
    public void testCardSuccessfullyRemoved() throws
            EntityNotFoundException, InvalidOrderException, CardNotFoundException {
        Player l_player1 = d_playerList.get(0);
        List<Country> l_player1AssignCountries = l_player1.getAssignedCountries();
        l_player1.addCard(new BlockadeCard());
        BlockadeOrder l_blockadeOrder = new BlockadeOrder(l_player1AssignCountries.get(0).getCountryName(), l_player1);
        l_blockadeOrder.execute();

        //Now during second execution player will not have a blockade card as we have assigned only one blockade card manually.
        //So it will raise InvalidCommandException.
        l_blockadeOrder.execute();
    }
}

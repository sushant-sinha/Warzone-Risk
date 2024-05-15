package com.APP.Project.UserCoreLogic.game_entities.orders;

import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.constants.enums.StrategyType;
import com.APP.Project.UserCoreLogic.exceptions.*;
import com.APP.Project.UserCoreLogic.gamePlay.GamePlayEngine;
import com.APP.Project.UserCoreLogic.map_features.adapters.EditMapService;
import com.APP.Project.Main;
import com.APP.Project.UserCoreLogic.game_entities.Country;
import com.APP.Project.UserCoreLogic.game_entities.Player;
import com.APP.Project.UserCoreLogic.game_entities.cards.AirliftCard;
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
 * This class is for testing various operations performed during the execution of the airlift command.
 *
 * @author Sushant Sinha
 */
public class AirliftOrderTest {
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
     * checks that execute method working properly.
     *
     * @throws EntityNotFoundException  Throws if entity not found.
     * @throws InvalidArgumentException Throws if the input is invalid.
     * @throws InvalidOrderException    Throws if exception while executing the order.
     * @throws CardNotFoundException    Card doesn't found in the player's card list.
     */
    @Test
    public void testExecute()
            throws EntityNotFoundException, InvalidArgumentException, InvalidOrderException, CardNotFoundException {
        Player l_player = d_playerList.get(0);
        List<Country> l_playerAssignCountries = l_player.getAssignedCountries();
        l_playerAssignCountries.get(0).setNumberOfArmies(7);
        l_playerAssignCountries.get(1).setNumberOfArmies(5);
        int l_expected = l_playerAssignCountries.get(1).getNumberOfArmies();
        l_player.addCard(new AirliftCard());

        AirliftOrder l_airliftOrder = new AirliftOrder(l_playerAssignCountries.get(0).getCountryName(), l_playerAssignCountries.get(1).getCountryName(), "2", l_player);
        l_airliftOrder.execute();
        assertEquals(l_playerAssignCountries.get(1).getNumberOfArmies(), l_expected + 2);
    }

    /**
     * checks that player has airlift card.
     *
     * @throws EntityNotFoundException  Throws if entity not found.
     * @throws InvalidArgumentException Throws if the input is invalid.
     * @throws InvalidOrderException    Throws if exception while executing the order.
     * @throws CardNotFoundException    Card doesn't found in the player's card list.
     */
    //not have airlift card
    @Test(expected = CardNotFoundException.class)
    public void testPlayerHasCard()
            throws EntityNotFoundException, InvalidArgumentException, InvalidOrderException, CardNotFoundException {
        Player l_player = d_playerList.get(0);
        List<Country> l_playerAssignCountries = l_player.getAssignedCountries();
        l_playerAssignCountries.get(0).setNumberOfArmies(7);
        l_playerAssignCountries.get(1).setNumberOfArmies(5);
        int l_expected = l_playerAssignCountries.get(1).getNumberOfArmies();
        l_player.addCard(new BombCard());

        AirliftOrder l_airliftOrder = new AirliftOrder(l_playerAssignCountries.get(0).getCountryName(), l_playerAssignCountries.get(1).getCountryName(), "2", l_player);
        l_airliftOrder.execute();
    }

    /**
     * checks that player will not airlift armies in others country.
     *
     * @throws EntityNotFoundException  Throws if entity not found.
     * @throws InvalidArgumentException Throws if the input is invalid.
     * @throws InvalidOrderException    Throws if exception while executing the order.
     * @throws CardNotFoundException    Card doesn't found in the player's card list.
     */
    //try to transfer into other's country
    @Test(expected = InvalidOrderException.class)
    public void testPlayerNotAirliftInOthersCountry()
            throws EntityNotFoundException, InvalidArgumentException, InvalidOrderException, CardNotFoundException {
        Player l_player = d_playerList.get(0);
        Player l_player2 = d_playerList.get(1);
        List<Country> l_playerAssignCountries = l_player.getAssignedCountries();
        List<Country> l_playerAssignCountries1 = l_player2.getAssignedCountries();
        l_player.addCard(new AirliftCard());

        AirliftOrder l_airliftOrder = new AirliftOrder(l_playerAssignCountries.get(0).getCountryName(), l_playerAssignCountries1.get(0).getCountryName(), "2", l_player);
        l_airliftOrder.execute();
    }

    /**
     * method test that source country has entered number of armies for airlifted.
     *
     * @throws EntityNotFoundException  Throws if entity not found.
     * @throws InvalidArgumentException Throws if the input is invalid.
     * @throws InvalidOrderException    Throws if exception while executing the order.
     * @throws CardNotFoundException    Card doesn't found in the player's card list.
     */
    @Test(expected = InvalidOrderException.class)
    public void testPlayerHasEnteredArmies()
            throws EntityNotFoundException, InvalidArgumentException, InvalidOrderException, CardNotFoundException {
        Player l_player = d_playerList.get(0);
        List<Country> l_playerAssignCountries = l_player.getAssignedCountries();
        l_playerAssignCountries.get(0).setNumberOfArmies(7);
        l_playerAssignCountries.get(1).setNumberOfArmies(5);
        int l_expected = l_playerAssignCountries.get(1).getNumberOfArmies();
        l_player.addCard(new AirliftCard());

        AirliftOrder l_airliftOrder = new AirliftOrder(l_playerAssignCountries.get(0).getCountryName(), l_playerAssignCountries.get(1).getCountryName(), "8", l_player);
        l_airliftOrder.execute();
    }
}
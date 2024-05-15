package com.APP.Project.UserCoreLogic.gamePlay;

import com.APP.Project.UserCoreLogic.gamePlay.services.ReinforcementServiceTest;
import com.APP.Project.UserCoreLogic.gamePlay.services.CountryDistributionServiceTest;
import com.APP.Project.UserCoreLogic.gamePlay.services.DisplayMapServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite for the game play functionality.
 * This suite includes tests for reinforcement service, country distribution service, and display map service.
 *
 * @author Rupal Kapoor
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ReinforcementServiceTest.class,
        CountryDistributionServiceTest.class,
        DisplayMapServiceTest.class
})
public class GamePlayTestSuite {
    // This class remains empty, it is used only as a holder for the above annotations
}

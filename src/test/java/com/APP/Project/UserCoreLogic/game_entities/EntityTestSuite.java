package com.APP.Project.UserCoreLogic.game_entities;

import com.APP.Project.UserCoreLogic.game_entities.orders.*;
import com.APP.Project.UserCoreLogic.game_entities.strategy.AggressiveStrategyTest;
import com.APP.Project.UserCoreLogic.game_entities.strategy.BenevolentStrategyTest;
import com.APP.Project.UserCoreLogic.game_entities.strategy.CheaterStrategyTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite for test cases of the different concrete classes of <code>Order</code> and other entities.
 *
 * @author Sushant Sinha
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AdvanceOrderTest.class,
        AirliftOrderTest.class,
        BlockadeOrderTest.class,
        BombOrderTest.class,
        DeployOrderTest.class,
        PlayerTest.class,
        NegotiateOrderTest.class,
        AggressiveStrategyTest.class,
        BenevolentStrategyTest.class,
        CheaterStrategyTest.class
})
public class EntityTestSuite {
    // This class remains empty, it is used only as a holder for the above annotations
}

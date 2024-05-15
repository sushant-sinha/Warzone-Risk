package com.APP.Project.UserCoreLogic;

import com.APP.Project.UserCoreLogic.game_entities.EntityTestSuite;
import com.APP.Project.UserCoreLogic.gamePlay.GamePlayTestSuite;
import com.APP.Project.UserCoreLogic.map_features.MapEditorTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite for <code>UserCoreLogic</code> test cases.
 *
 * @author Bhoomiben Bhatt
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        GamePlayTestSuite.class,
        MapEditorTestSuite.class,
        EntityTestSuite.class
})
public class UserCoreLogicTestSuite {
    // This class remains empty, it is used only as a holder for the above annotations.
}

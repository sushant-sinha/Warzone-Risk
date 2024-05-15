package com.APP.Project;
/**
 * Central test suite for running all test cases of the application.
 *
 */

import com.APP.Project.UserInterface.UserInterfaceTestSuite;
import com.APP.Project.UserCoreLogic.UserCoreLogicTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite for <code>UserInterface</code> and <code>UserCoreLogic</code> test cases.
 *
 * @author Bhoomiben Bhatt
 * @version 1.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        UserInterfaceTestSuite.class,
        UserCoreLogicTestSuite.class
})
public class ApplicationTestSuite {
    // This class remains empty, it is used only as a holder for the above annotations.
}

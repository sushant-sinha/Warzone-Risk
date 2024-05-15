package com.APP.Project.UserInterface;

import com.APP.Project.UserInterface.constants.CommandsTestCases;
import com.APP.Project.UserInterface.mappers.UsersCommandsMapperTest;
import com.APP.Project.UserInterface.service.RequestsServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suit for command-line interface package.
 *
 * @author Bhoomiben Bhatt
 * @version 1.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        CommandsTestCases.class,
        UsersCommandsMapperTest.class,
        RequestsServiceTest.class,
})
public class UserInterfaceTestSuite {
    // This class remains empty, it is used only as a holder for the above annotations
}

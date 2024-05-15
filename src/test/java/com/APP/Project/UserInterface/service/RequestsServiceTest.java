package com.APP.Project.UserInterface.service;

import com.APP.Project.Main;
import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserInterface.layouts.PlayerCommandLayout;
import com.APP.Project.UserInterface.models.UsersCommands;
import com.APP.Project.UserInterface.service.RequestsService;
import com.APP.Project.UserCoreLogic.GameEngine;
import com.APP.Project.UserCoreLogic.phases.PostLoad;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

/**
 * Tests the processing and execution of interpreted user commands within the
 * application.
 * This includes validating that commands are correctly interpreted from user
 * input and that the appropriate actions are taken based on those commands.
 * It ensures that the application responds correctly to user commands,
 * executing the expected functionality with the provided arguments.
 *
 * @author Bhoomiben Bhatt
 * @version 1.0
 */
public class RequestsServiceTest {
    private UsersCommands d_userCommand;

    /**
     * Initializes the application context before any tests are run.
     * This setup is necessary to ensure that the application environment is ready
     * for executing the commands as intended.
     */
    @BeforeClass
    public static void beforeClass() {
        Main l_application = new Main();
        l_application.handleApplicationStartup();
    }

    /**
     * Prepares the necessary context for testing by initializing test data and
     * setting up the application state.
     * This includes configuring a specific user command and its arguments to be
     * used in the test execution.
     */
    @Before
    public void before() {
        d_userCommand = new UsersCommands(PlayerCommandLayout.matchAndGetUserCommand("editcontinent"));

        // When user enters editcontinent -add continentID 12
        d_userCommand.pushUserArgument("add",
                Arrays.asList("continentID", "12"));

        Main l_application = new Main();
        l_application.handleApplicationStartup();

        GameEngine l_gameEngine = UserCoreLogic.getGameEngine();

        l_gameEngine.initialise();
        l_gameEngine.setGamePhase(new PostLoad(l_gameEngine));
    }

    /**
     * Tests the execution of a user command through the RequestsService.
     * The method verifies that the command can be processed and executed without
     * any exceptions,
     * which indicates the application can handle the command as expected.
     */
    @Test(expected = Test.None.class /* no exception expected */)
    public void testTakeAction() {
        // If the method call completes without any raised exception, then the call was successful
        RequestsService l_requestService = new RequestsService();
        l_requestService.takeAction(d_userCommand);
    }
}

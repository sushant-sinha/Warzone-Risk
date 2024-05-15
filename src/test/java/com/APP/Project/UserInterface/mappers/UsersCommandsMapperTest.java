package com.APP.Project.UserInterface.mappers;

import com.APP.Project.UserInterface.layouts.PlayerCommandLayout;
import com.APP.Project.UserInterface.models.UsersCommands;
import com.APP.Project.Main;
import com.APP.Project.UserCoreLogic.UserCoreLogic;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Class to test the interpreted value of the user input text
 */
public class UsersCommandsMapperTest {
    /**
     * User input text
     */
    private String d_commandWithArgument;
    private String d_commandWithValue;

    /**
     * Correct interpreted user command of the input text
     */
    private UsersCommands d_correctCommandWithArgument;
    private UsersCommands d_correctCommandWithValue;

    /**
     * Sets the application context
     */
    @BeforeClass
    public static void beforeClass() {
        Main l_application = new Main();
        l_application.handleApplicationStartup();
    }

    /**
     * Sets the required context before executing test case.
     */
    @Before
    public void before() {
        UserCoreLogic.getGameEngine().initialise();
        d_commandWithArgument = "editcontinent -add Canada 10 -remove Continent";
        d_correctCommandWithArgument = new UsersCommands(PlayerCommandLayout.matchAndGetUserCommand("editcontinent"));
        d_correctCommandWithArgument.pushUserArgument("add",
                Arrays.asList("continentID", "continentvalue"));
        d_correctCommandWithArgument.pushUserArgument("remove",
                Collections.singletonList("continentID"));

        d_commandWithValue = "savemap filename warzone";
        d_correctCommandWithValue = new UsersCommands(PlayerCommandLayout.matchAndGetUserCommand("savemap"));
        List<String> l_commandValues = new ArrayList<>();
        l_commandValues.add("filename");
        l_commandValues.add("warzone");
        d_correctCommandWithValue.setCommandValues(l_commandValues);

        // Sets the game state to MAP_EDITOR
        Main l_application = new Main();
        l_application.handleApplicationStartup();
    }

    /**
     * Creates user input and tests whether it is correct or not.
     */
    @Test
    public void testUserInput() {
        // Mapper class which maps text to interpreted command.
        UserCommandsMapper l_userCommandMapper = new UserCommandsMapper();

        // Interprets the user text
        UsersCommands l_commandWithArgument = l_userCommandMapper.toUserCommand(this.d_commandWithArgument);
        UsersCommands l_commandWithValue = l_userCommandMapper.toUserCommand(this.d_commandWithValue);

        assertEquals(l_commandWithArgument, d_correctCommandWithArgument);
        assertEquals(l_commandWithValue, d_correctCommandWithValue);
    }
}

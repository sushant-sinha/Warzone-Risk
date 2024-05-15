package com.APP.Project.UserInterface.constants;

import com.APP.Project.UserInterface.layouts.PlayerCommandLayout;
import com.APP.Project.Main;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests if a command's text is correct for the game's current state.
 * @author Raj Kumar Ramesh
 * @version 1.0
 */
public class CommandsTestCases {
    /**
     * The command to test.
     */
    String d_headOfCommand;
    /**
     * A correct parameter for the command.
     */
    String d_argKey;
    /**
     * An incorrect parameter for the command.
     */
    String d_wrongArgKey;

    /**
     * Prepares the testing environment.
     */
    @Before
    public void before() {
        d_headOfCommand = "editcontinent";
        d_argKey = "-add";
        d_wrongArgKey = "-delete";
        // Sets the game's state to MAP_EDITOR because it matches d_headOfCommand
        Main l_application = new Main();
        l_application.handleApplicationStartup();
    }

    /**
     * Verifies if a parameter is valid for a command.
     * <p>
     * Checks that the right and wrong parameters are correctly identified.
     */
    @Test
    public void testIsKeyOfCommand() {
        // Tests if 'add' is a valid parameter for 'editcontinent'
        boolean isKeyOfEditContent = PlayerCommandLayout.matchAndGetUserCommand(d_headOfCommand).isKeyOfCommand(this.d_argKey);

        // Tests if 'delete' is not a valid parameter for 'editcontinent'
        boolean isNotKeyOfEditContent = PlayerCommandLayout.matchAndGetUserCommand(d_headOfCommand).isKeyOfCommand(this.d_wrongArgKey);

        assertTrue(isKeyOfEditContent);
        assertFalse(isNotKeyOfEditContent);
    }
}
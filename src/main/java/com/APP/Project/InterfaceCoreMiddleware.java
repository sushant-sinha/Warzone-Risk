package com.APP.Project;

/**
 * This interface asks user to enter the command string
 *
 * @author Jayati Thakkar
 * @version 1.0
 */

public interface InterfaceCoreMiddleware {

    /**
     *
     * @param p_message message that asks user to input
     * @return          returns the response
     */

    String askForUserInput(String p_message);

    /**
     * user is shown the message using this interface
     *
     * @param p_message this is the message to print
     */

    void stdout(String p_message);

    /**
     * whenever an error occurs, this issue is shown to the user
     *
     * @param p_message the message that is to be shown to the user
     */
    void stderr(String p_message);

}

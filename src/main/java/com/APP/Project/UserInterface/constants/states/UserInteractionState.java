package com.APP.Project.UserInterface.constants.states;

/**
 * This class specifies the various user interactions: user waiting - execution or program awaiting user input
 *
 * @author Raj Kumar Ramesh
 * @version 1.0
 */
public enum UserInteractionState {
    /**
     * Waiting for userinput.
     */
    WAIT("wait"),
    /**
     * User waiting for command to execute.
     */
    IN_PROGRESS("in_progress"),
    /**
     * GameEngine has control over user input request.
     */
    GAME_ENGINE("game_engine");

    /**
     * State Value.
     */
    public String d_jsonValue;

    /**
     * Parameterised constructor to set json value.
     *
     * @param p_jsonValue
     */
    private UserInteractionState(String p_jsonValue) {
        this.d_jsonValue = p_jsonValue;
    }

    /**
     * Gets enum string value
     *
     * @return Value of enum
     */
    public String getJsonValue() {
        return d_jsonValue;
    }
}

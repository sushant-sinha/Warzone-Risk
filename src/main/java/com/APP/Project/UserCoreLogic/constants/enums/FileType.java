package com.APP.Project.UserCoreLogic.constants.enums;

/**
 * This enum lists all the orders which player can issue during <code>issue orders</code> phase.
 * represent the command which can be entered by the player at the <code>GameLoop#ISSUE_ORDER</code> phase.
 *
 * @author Bhoomiben Bhatt, Jayati Thakkar
 * @version 1.0
 */
public enum FileType {

    MAP("map"),
    GAME("game");

    /**
     * Variable to set enum value.
     */
    public String d_jsonValue;

    /**
     * Sets the string value of the enum.
     *
     * @param p_jsonValue Value of the enum.
     */
    private FileType(String p_jsonValue) {
        this.d_jsonValue = p_jsonValue;
    }

    /**
     * Gets the string value of the enum
     *
     * @return Value of the enum
     */
    public String getJsonValue() {
        return d_jsonValue;
    }
}

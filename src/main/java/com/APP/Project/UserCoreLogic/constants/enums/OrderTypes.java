package com.APP.Project.UserCoreLogic.constants.enums;

/**
 * This enum lists all the orders which player can issue during <code>issue orders</code> phase.
 * represent the command which can be entered by the player at the <code>GameLoop#ISSUE_ORDER</code> phase.
 *
 * @author Bhoomiben Bhatt
 * @version 1.0
 */
public enum OrderTypes {
    /**
     * If order type is advance.
     */
    advance("advance"),
    /**
     * If order type is airlift.
     */
    airlift("airlift"),
    /**
     * If order type is blockade.
     */
    blockade("blockade"),
    /**
     * If order type is bomb.
     */
    bomb("bomb"),
    /**
     * If order type is deploy.
     */
    deploy("deploy"),
    /**
     * If order type is negotiate.
     */
    negotiate("negotiate");

    /**
     * Variable to set enum value.
     */
    public String d_jsonValue;

    /**
     * Sets the string value of the enum.
     *
     * @param p_jsonValue Value of the enum.
     */
    private OrderTypes(String p_jsonValue) {
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

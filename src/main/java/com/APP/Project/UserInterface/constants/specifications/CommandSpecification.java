package com.APP.Project.UserInterface.constants.specifications;

/**
 * Specification types for command to run
 *
 * @author Raj Kumar Ramesh
 * @version 1.0
 */
public enum CommandSpecification {
    /**
     *  No Arguements needed for this command to run
     */
    CAN_RUN_ALONE,
    /**
     * Arguement values required for the command to run
     * For this specification, command layout will use ArgumentsSpecification to indicate # of arguments it needs to run
     */
    CAN_RUN_ALONE_WITH_VALUE,
    /**
     * This command requires one or more argument keys and their corresponding values for execution.
     * In accordance with this specification, the command structure also utilizes the ArgumentSpecification to define the necessary number of arguments for execution.
     */
    NEEDS_KEYS
}
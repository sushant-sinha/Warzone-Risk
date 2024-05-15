package com.APP.Project.UserInterface.models;

import com.APP.Project.UserInterface.constants.specifications.CommandSpecification;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

/**
 * This class is used to understand and process the command entered by the user
 *
 * @author Jayati Thakkar
 * @version 1.0
 */
public class UsersCommands {

    /**
     * the header of the command
     */
    private String d_headCommand;

    /**
     * a map of the keys and its values for the arguments the user has to enter
     */
    private List<Map<String, List<String>>> d_userArguments;

    /**
     * the list of the command related values
     */
    private List<String> d_commandValues;

    /**
     * an object of the predefined user commands class
     */
    private PredefinedUserCommands d_predefinedUserCommand;

    /**
     * true if the command is <code> exit </code>
     */
    private boolean d_isExitCommand = false;

    /**
     * default constructor
     */
    public UsersCommands() {

    }

    /**
     * parameterized constructor
     *
     * @param p_predefinedUserCommand an object of the Predefined user commands class
     */
    public UsersCommands(PredefinedUserCommands p_predefinedUserCommand) {
        setHeadCommand(p_predefinedUserCommand.getHeadCommand());
        d_predefinedUserCommand = p_predefinedUserCommand;
        // Initialise references
        d_userArguments = new ArrayList<>();
        d_commandValues = new ArrayList<>();
    }

    /**
     * getter for header
     * @return the header of the command
     */
    public String getHeadCommand() {
        return d_headCommand;
    }

    /**
     * sets the header of the command into its variable
     * @param p_headCommand a string containing the header of the command
     */
    private void setHeadCommand(String p_headCommand) {
        d_headCommand = p_headCommand;
    }

    /**
     * it gets all the userrguments and returns the key and value of those lists
     * @return List of user arguments
     */
    @JsonIgnore
    public List<Map<String, List<String>>> getUserArguments() {
        return d_userArguments;
    }

    /**
     * it adds the value to the user argument mappings.
     *
     * @param argKey argument key value
     * @param values argument key list values
     */
    public void pushUserArgument(String argKey, List<String> values) {
        Map<String, List<String>> l_newArgumentKeyValue = new HashMap<>();
        l_newArgumentKeyValue.put(argKey, values);
        d_userArguments.add(l_newArgumentKeyValue);
    }

    /**
     * getter for the command related values
     * @return the list of string containing the values possible
     */
    public List<String> getCommandValues() {
        return d_commandValues;
    }

    /**
     * setter to set the command related values
     *
     * @param d_commandValues the new value for command
     */
    public void setCommandValues(List<String> d_commandValues) {
        this.d_commandValues = d_commandValues;
    }

    /**
     * it gets predefined version of the users command.
     * @return the version of predefined form
     */
    @JsonIgnore
    public PredefinedUserCommands getPredefinedUserCommand() {
        return d_predefinedUserCommand;
    }

    /**
     * overridden equals method to check if it equal
     * @param p_l_o object that needs to be compared
     * @return true if it is equal; false otherwise
     */
    @Override
    public boolean equals(Object p_l_o) {
        if (this == p_l_o) return true;
        if (p_l_o == null || getClass() != p_l_o.getClass()) return false;
        UsersCommands l_that = (UsersCommands) p_l_o;
        return d_isExitCommand == l_that.d_isExitCommand &&
                Objects.equals(d_headCommand, l_that.d_headCommand) &&
                d_userArguments.size() == l_that.d_userArguments.size() &&
                Objects.equals(d_predefinedUserCommand, l_that.d_predefinedUserCommand);
    }
}
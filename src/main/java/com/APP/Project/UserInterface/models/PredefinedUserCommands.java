package com.APP.Project.UserInterface.models;

import com.APP.Project.UserInterface.constants.specifications.ArgumentSpecification;
import com.APP.Project.UserInterface.constants.specifications.CommandSpecification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * this provides a structure for predefined commands the user is supposed to enter
 *
 * @author Jayati Thakkar
 * @version 1.0
 */
public class PredefinedUserCommands {

    private static final String D_ARG_PREFIX = "-";
    private String d_headCommand;

    private final List<CommandLineArgument> d_commandArgumentList;

    private CommandSpecification d_commandSpecification;

    private int d_numOfValues = 1;

    private ArgumentSpecification d_commandKeySpecification = ArgumentSpecification.MIN;

    private boolean d_isGameEngineCommand = false;

    private boolean d_isOrderCommand = false;

    private String d_gamePhaseMethodName;

    /**
     * It initializes all the members.
     */
    public PredefinedUserCommands() {
        // Initialise references
        d_commandArgumentList = new ArrayList<>();
    }

    /**
     * gives the head of the command
     * @return returns the head of the command
     */
    public String getHeadCommand() {
        return d_headCommand;
    }

    /**
     * sets the heafer command
     * @param p_headCommand string value that will initialize the head variable
     */
    public void setHeadCommand(String p_headCommand) {
        d_headCommand = p_headCommand;
    }


    /**
     * returns the list of arguments
     *
     * @return the list of arguments
     */
    public List<CommandLineArgument> getCommandArgumentList() {
        return d_commandArgumentList;
    }

    /**
     * add elements to the list of commandLineArgument object.
     * @param p_commandArgument An object of the same class
     */
    public void pushCommandArgument(CommandLineArgument p_commandArgument) {
        d_commandArgumentList.add(p_commandArgument);
    }

    /**
     * returns the list of rguments
     * @return the list of the keys of the arguments
     */
    public List<String> getArgumentKeys() {
        return this.d_commandArgumentList.stream().map((CommandLineArgument::getArgumentKey))
                .collect(Collectors.toList());
    }

    /**
     * it tries matching the string variable with the list available for the possible arguments for the given command
     *
     * @param p_argumentKey the variable that needs the command to be checked
     * @return alue of available list
     */
    public CommandLineArgument matchCommandArgument(String p_argumentKey) {
        // Returns only one element
        return this.d_commandArgumentList.stream().filter((p_p_argumentKey) ->
                p_argumentKey.equals(PredefinedUserCommands.D_ARG_PREFIX.concat(p_p_argumentKey.getArgumentKey()))
        ).collect(Collectors.toList()).get(0);
    }

    /**
     * checks if it is the key for the command
     *
     * @param p_argKey the string that needs to be checked
     * @return it returns true if it martches; false otherwise
     */
    public boolean isKeyOfCommand(String p_argKey) {
        if (!p_argKey.startsWith(PredefinedUserCommands.D_ARG_PREFIX))
            return false;
        return this.getArgumentKeys().stream().anyMatch((p_p_argKey) ->
                p_argKey.equals(PredefinedUserCommands.D_ARG_PREFIX.concat(p_p_argKey))
        );
    }

    /**
     * setter
     * @param p_commandSpecification CommandSpecification object
     */
    public void setCommandSpecification(CommandSpecification p_commandSpecification) {
        this.d_commandSpecification = p_commandSpecification;
    }

    /**
     * getter
     * @return returns the CommandSpecification object
     */
    public CommandSpecification getCommandSpecification() {
        return d_commandSpecification;
    }

    /**
     * it decides the number of values to have with the command
     * @return total number of values
     */
    public int getNumOfKeysOrValues() {
        return d_numOfValues;
    }

    /**
     * sets the number of values to have with the command
     *
     * @param p_numOfValues the number of values to have with the command is set
     */
    public void setNumOfValues(int p_numOfValues) {
        d_numOfValues = p_numOfValues;
    }

    /**
     * checks if it is a command
     * @return true if is it a command; false otherwise
     */
    public boolean isGameEngineCommand() {
        return d_isGameEngineCommand;
    }

    /**
     * setter only if the engine can ask for the user input
     * @param p_gameEngineCommand true if the user can be asked; false otherwise
     */
    public void setGameEngineCommand(boolean p_gameEngineCommand) {
        d_isGameEngineCommand = p_gameEngineCommand;
    }

    /**
     * if the command is an order command
     * @return True if it is an order command; false otherwise
     */
    public boolean isOrderCommand() {
        return d_isOrderCommand;
    }

    /**
     * Sets true if the command is an order command.
     *
     * @param p_orderCommand the command type.
     */
    public void setOrderCommand(boolean p_orderCommand) {
        d_isOrderCommand = p_orderCommand;
    }

    /**
     * Gets the method name to be called for this user command.
     *
     * @return Value of the method name.
     */
    public String getGamePhaseMethodName() {
        return d_gamePhaseMethodName;
    }

    /**
     * Sets the method name to be called for this user command.
     *
     * @param p_gamePhaseMethodName Value of the method name.
     */
    public void setGamePhaseMethodName(String p_gamePhaseMethodName) {
        d_gamePhaseMethodName = p_gamePhaseMethodName;
    }

    /**
     * overridden equals method to check if it equal
     * @param l_p_o object that needs to be compared
     * @return true if it is equal; false otherwise
     */
    @Override
    public boolean equals(Object l_p_o) {
        if (this == l_p_o) return true;
        if (l_p_o == null || getClass() != l_p_o.getClass()) return false;
        PredefinedUserCommands l_that = (PredefinedUserCommands) l_p_o;
        return Objects.equals(d_headCommand, l_that.d_headCommand) &&
                Objects.equals(d_commandArgumentList, l_that.d_commandArgumentList);
    }

    /**
     * Sets the specification for the number of required keys for <code>CommandSpecification#NEED_KEYS</code> command.
     *
     * @param d_commandKeySpecification Specification for <code>CommandSpecification#NEED_KEYS</code> command.
     */
    public void setCommandKeySpecification(ArgumentSpecification d_commandKeySpecification) {
        this.d_commandKeySpecification = d_commandKeySpecification;
    }

    /**
     * Gets the specification for the number of required keys for <code>CommandSpecification#NEED_KEYS</code> command.
     *
     * @return Specification for <code>CommandSpecification#NEED_KEYS</code> command.
     */
    public ArgumentSpecification getCommandKeySpecification() {
        return d_commandKeySpecification;
    }


}

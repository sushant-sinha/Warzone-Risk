package com.APP.Project.UserInterface.models;

import com.APP.Project.UserInterface.constants.specifications.ArgumentSpecification;

import java.util.Objects;

/**
 * This class provides a structure for command line argument of the command
 *
 * @author Jayati Thakkar
 * @version 1.0
 */
public class CommandLineArgument {

    private String d_argumentKey;
    private int d_numOfValues;
    private ArgumentSpecification d_specification;

    /**
     *
     * parameterized constructor
     *
     *
     * @param p_argumentKey
     * @param p_numOfValues
     * @param p_specification
     */
    public CommandLineArgument(String p_argumentKey, int p_numOfValues, ArgumentSpecification p_specification) {
        d_argumentKey = p_argumentKey;
        d_numOfValues = p_numOfValues;
        d_specification = p_specification;
    }

    //TODO: Fix javadoc comments

    /**
     * getters and setters for all the attributes
     *
     * @return
     */
    public String getArgumentKey() {
        return d_argumentKey;
    }

    /**
     * @param p_argumentKey
     */
    public void setArgumentKey(String p_argumentKey) {
        d_argumentKey = p_argumentKey;
    }

    /**
     * @return
     */
    public int getNumOfValues() {
        return d_numOfValues;
    }

    /**
     * @param p_numOfValues
     */
    public void setNumOfValues(int p_numOfValues) {
        d_numOfValues = p_numOfValues;
    }

    /**
     * @return
     */
    public ArgumentSpecification getSpecification() {
        return d_specification;
    }

    /**
     * @param p_specification
     */
    public void setSpecification(ArgumentSpecification p_specification) {
        d_specification = p_specification;
    }

    /**
     * checks if the commands arguments are equal or not
     *
     * @param l_p_o  provided object to check
     * @return true if it is same; false otherwise
     */

    @Override
    public boolean equals(Object l_p_o) {
        if (this == l_p_o) return true;
        if (l_p_o == null || getClass() != l_p_o.getClass()) return false;
        CommandLineArgument l_that = (CommandLineArgument) l_p_o;
        return d_numOfValues == l_that.d_numOfValues &&
                Objects.equals(d_argumentKey, l_that.d_argumentKey) &&
                d_specification == l_that.d_specification;
    }
}


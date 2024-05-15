package com.APP.Project.UserCoreLogic.logger;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a buffer for logging entries and implements the Observable interface.
 * It allows attaching and detaching observers and notifying them upon data changes.
 *
 * @author Rikin Dipakkumar Chauhan
 * @version 1.0
 */
public class LogEntryBuffer implements Observable {
    private final List<Observer> d_observerList;
    private String d_message;
    private String d_headCommand;
    private static LogEntryBuffer d_instance;

     /**
     * Private constructor to prevent instantiation from outside the class.
     */
    private LogEntryBuffer() {
        d_observerList = new ArrayList<>();
    }

    /**
     * Retrieves the singleton instance of LogEntryBuffer.
     *
     * @return the singleton instance of LogEntryBuffer
     */
    public static LogEntryBuffer getLogger() {
        if (d_instance == null) {
            d_instance = new LogEntryBuffer();
        }
        return d_instance;
    }

    /**
     * Attaches an observer to the list of observers.
     *
     * @param p_observer The observer to attach
     */
    @Override
    public void attach(Observer p_observer) {
        d_observerList.add(p_observer);
    }

    /**
     * Detaches an observer from the list of observers.
     *
     * @param p_observer The observer to detach
     */
    @Override
    public void detach(Observer p_observer) {
        d_observerList.remove(p_observer);
    }

    /**
     * Notifies all observers with the provided Observable object.
     *
     * @param p_o The Observable object to notify observers with
     */
    @Override
    public void notifyObservers(Observable p_o) {
        for (Observer l_observer : d_observerList) {
            l_observer.update(p_o);
        }
    }

    /**
     * Retrieves the message stored in the buffer.
     *
     * @return The message stored in the buffer.
     */
    public String getMessage() {
        return String.format("---%s---\n%s\n", this.getHeadCommand(), d_message);
    }

    /**
     * Sets the message in the buffer.
     *
     * @param p_message The message to be set in the buffer.
     */
    public void setMessage(String p_message) {
        d_message = p_message;
    }

    /**
     * Updates the head command and message in the buffer and notifies observers.
     *
     * @param p_headCommand The head command to be set in the buffer.
     * @param p_message     The message to be set in the buffer.
     */
    public void dataChanged(String p_headCommand, String p_message) {
        d_headCommand = p_headCommand;
        d_message = p_message;
        notifyObservers(this);
    }

    /**
     * Retrieves the head command stored in the buffer.
     *
     * @return The head command stored in the buffer.
     */
    public String getHeadCommand() {
        return d_headCommand;
    }

     /**
     * Sets the head command in the buffer.
     *
     * @param p_headCommand The head command to be set in the buffer.
     */
    public void setHeadCommand(String p_headCommand) {
        d_headCommand = p_headCommand;
    }
}
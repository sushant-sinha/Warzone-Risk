package com.APP.Project.UserCoreLogic.constants.interfaces;

/**
 * Serves as the core for various engines, managing runtime details of players and the game itself.
 *
 * Usage:
 *  Obtain an instance via the static `getInstance` method in a subclass.
 *  Each game should have only one instance created by the subclass.
 *
 * @author Bhoomiben Bhatt
 * @version 1.0
 */
public interface Engine {
    /**
     * Initialise the engine and all its data members.
     */
    void initialise();

    /**
     * Stop and exit from all the threads  of <code>Engine</code>.
     */
    void shutdown();
}

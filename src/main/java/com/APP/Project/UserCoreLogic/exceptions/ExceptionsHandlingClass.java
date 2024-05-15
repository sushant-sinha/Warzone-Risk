package com.APP.Project.UserCoreLogic.exceptions;

import com.APP.Project.UserCoreLogic.UserCoreLogic;

/**
 * This class handles uncaught exceptions in the application.
 * It implements the Thread.UncaughtExceptionHandler interface.
 *
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class ExceptionsHandlingClass implements Thread.UncaughtExceptionHandler {
    
    /**
     * This method is called when an uncaught exception occurs.
     * It logs the error message using UserCoreLogic's stderr method.
     *
     * @param t The thread where the exception occurred.
     * @param e The uncaught exception.
     */
    public void uncaughtException(Thread t, Throwable e) {
        UserCoreLogic.getInstance().stderr("Something went wrong!");
    }
}
package com.APP.Project.UserCoreLogic.logger;

import com.APP.Project.UserCoreLogic.exceptions.InvalidInputException;
import com.APP.Project.UserCoreLogic.exceptions.ResourceNotFoundException;

import java.io.IOException;

/**
 * Interface representing an Observable object that can be observed by Observer objects.
 *
 * @author Rikin Dipakkumar Chauhan
 * @version 1.0
 */
public interface Observable {

    /**
     * Attaches an Observer to this Observable object.
     *
     * @param o the Observer to attach
     */
    void attach(Observer o);

    /**
     * Detaches an Observer from this Observable object.
     *
     * @param o the Observer to detach
     */
    void detach(Observer o);

    /**
     * Notifies all attached Observers of a change in this Observable object.
     *
     * @param p_o the Observable object triggering the notification
     * @throws ResourceNotFoundException if a required resource is not found
     * @throws IOException              if an I/O exception occurs
     * @throws InvalidInputException    if an invalid input is encountered
     */
    void notifyObservers(Observable p_o) throws ResourceNotFoundException, IOException, InvalidInputException;
}

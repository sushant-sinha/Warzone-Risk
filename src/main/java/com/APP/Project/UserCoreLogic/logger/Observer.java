package com.APP.Project.UserCoreLogic.logger;

/**
 * This abstract class represents an Observer in the observer design pattern.
 * Observers are notified when the state of the observed object changes.
 * 
 * @author Rikin Dipakkumar Chauhan
 * @version 1.0
 */
public abstract class Observer {
    
    /**
     * The Observable object being observed.
     */
    Observable d_observable;

    /**
     * Constructs an Observer object with the specified Observable object and attaches it to the Observable.
     * 
     * @param p_observable the Observable object to observe
     */
    public Observer(Observable p_observable) {
        d_observable = p_observable;
        d_observable.attach(this);
    }

    /**
     * This method is called by the Observable when its state changes.
     * Subclasses must implement this method to define their reaction to state changes.
     * 
     * @param p_observable_state the state of the Observable object
     */
    public abstract void update(Observable p_observable_state);
}
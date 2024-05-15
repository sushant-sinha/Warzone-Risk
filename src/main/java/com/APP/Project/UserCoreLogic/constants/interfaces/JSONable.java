package com.APP.Project.UserCoreLogic.constants.interfaces;

import com.APP.Project.UserCoreLogic.exceptions.InvalidGameException;
import org.json.JSONObject;

/**
 * * Defines methods for returning a <code>JSONObject</code> or assigning values to data members from a
 * <code>JSONObject</code>. This interface facilitates JSON-based data interchange.
 *
 * @author Bhoomiben Bhatt
 * @version 1.0
 */
public interface JSONable {
    /**
     * Constructs a <code>JSONObject</code> from the class's data members, reflecting current runtime information.
     *
     * @return The generated <code>JSONObject</code>.
     */
    JSONObject toJSON();

    /**
     * Instantiates this class and populates its fields with values from a <code>JSONObject</code>.
     *
     * @param p_jsonObject The <code>JSONObject</code> containing necessary data.
     * @throws InvalidGameException If the <code>JSONObject</code> is invalid or lacks required values.
     *
     */
    static void fromJSON(JSONObject p_jsonObject) throws InvalidGameException {
        throw new InvalidGameException("Concrete class didn't implement fromJSON method!");
    }
}

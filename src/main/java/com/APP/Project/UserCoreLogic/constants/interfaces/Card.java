package com.APP.Project.UserCoreLogic.constants.interfaces;

import com.APP.Project.UserCoreLogic.constants.enums.CardType;

import java.util.Date;

/**
 * An abstract base class for cards, enabling various actions like gaining armies, spying, and airlifting in gameplay.
 * Players earn card pieces by capturing territories, effective even if territories are lost later in the turn.
 *
 * @author Bhoomiben Bhatt
 * @version 1.0
 */
public abstract class Card {
    private final Date d_createdTime;


    /**
     * Constructor to assign its data members.
     */
    public Card() {
        d_createdTime = new Date();
    }

    /**
     * Retrieves the type of this card.
     *
     * @return The card type.
     */
    public abstract CardType getType();

    /**
     * Retrieves the timestamp representing when this class was instantiated.
     *
     * @return The creation timestamp of this class instance.
     */
    public long getCreatedTime() {
        return this.d_createdTime.getTime();
    }
}

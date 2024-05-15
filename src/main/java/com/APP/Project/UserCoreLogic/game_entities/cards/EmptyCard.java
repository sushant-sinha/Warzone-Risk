package com.APP.Project.UserCoreLogic.game_entities.cards;

import com.APP.Project.UserCoreLogic.constants.enums.CardType;
import com.APP.Project.UserCoreLogic.constants.interfaces.Card;

/**
 * This card does nothing.
 *
 * @author Sushant Sinha
 */
public class EmptyCard extends Card {
    /**
     * Constructor to assign its data members.
     */
    public EmptyCard() {
        super();
    }

    @Override
    public CardType getType() {
        return CardType.EMPTY;
    }
}

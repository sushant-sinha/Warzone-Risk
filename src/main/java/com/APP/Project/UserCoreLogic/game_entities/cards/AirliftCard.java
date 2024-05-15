package com.APP.Project.UserCoreLogic.game_entities.cards;

import com.APP.Project.UserCoreLogic.constants.enums.CardType;
import com.APP.Project.UserCoreLogic.constants.interfaces.Card;

/**
 * The Airlift Card permits the player to transfer their armies over long distances. A player can do a
 * single transfer from any of their territories to any other territory of their own or one of their teammates. This is similar to a
 * normal transfer. These armies are restricted and are not permitted to do any other action that turn. They must wait until the next turn.
 *
 * @author Sushant Sinha
 */
public class AirliftCard extends Card {
    /**
     * Constructor to assign its data members.
     */
    public AirliftCard() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CardType getType() {
        return CardType.AIRLIFT;
    }
}

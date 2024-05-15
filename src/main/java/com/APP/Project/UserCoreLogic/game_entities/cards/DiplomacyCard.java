package com.APP.Project.UserCoreLogic.game_entities.cards;

import com.APP.Project.UserCoreLogic.constants.enums.CardType;
import com.APP.Project.UserCoreLogic.constants.interfaces.Card;

/**
 * The Diplomacy Card enforces peace between two players for a variable number of turns. While peace is enforced,
 * neither player will be able to attack the other. The card takes effect the turn after it is played.
 *
 * @author Sushant Sinha
 */
public class DiplomacyCard extends Card {
    /**
     * Constructor to assign its data members.
     */
    public DiplomacyCard() {
        super();
    }

    @Override
    public CardType getType() {
        return CardType.DIPLOMACY;
    }
}

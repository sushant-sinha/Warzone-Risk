package com.APP.Project.UserCoreLogic.game_entities.cards;

import com.APP.Project.UserCoreLogic.constants.enums.CardType;
import com.APP.Project.UserCoreLogic.constants.interfaces.Card;

/**
 * The Bomb Card allows player to target an enemy or neutral territory and kill half of the armies on that territory.
 * Player can target any territory that’s adjacent to one of your own territories.
 *
 * @author Sushant Sinha
 */
public class BombCard extends Card {
    /**
     * Constructor to assign its data members.
     */
    public BombCard() {
        super();
    }

    @Override
    public CardType getType() {
        return CardType.BOMB;
    }
}

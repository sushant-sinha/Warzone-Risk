package com.APP.Project.UserCoreLogic.game_entities.cards;

import com.APP.Project.UserCoreLogic.constants.enums.CardType;
import com.APP.Project.UserCoreLogic.constants.interfaces.Card;

/**
 * The Blockade card works similarly to the emergency blockade card, however it is less effective. Instead of being active
 * at the beginning of your turn, it comes in effect at the end. This implies, any attacks, airlifts, or other actions happening
 * before the territory changes into a neutral and increases its armies.
 *
 * @author Sushant Sinha
 */
public class BlockadeCard extends Card {
    /**
     * Constructor to assign its data members.
     */
    public BlockadeCard() {
        super();
    }

    @Override
    public CardType getType() {
        return CardType.BLOCKADE;
    }
}

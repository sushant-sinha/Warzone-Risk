package com.APP.Project.UserCoreLogic.common.services;

import com.APP.Project.UserCoreLogic.constants.enums.CardType;
import com.APP.Project.UserCoreLogic.constants.interfaces.Card;
import com.APP.Project.UserCoreLogic.game_entities.cards.*;

import java.util.List;
import java.util.Random;

/**
 * This class provides services related to cards.
 *
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class CardService {

    /**
     * List of all usable card types.
     */
    public static List<CardType> d_CardList = CardType.usableCardList();

    /**
     * Generates a random card.
     *
     * @return A randomly generated card.
     */
    public static Card randomCard() {
        Random rand = new Random();
        // Getting a card-type using random index and creating a card using the type.
        return createCard(d_CardList.get(rand.nextInt(d_CardList.size())));
    }

    /**
     * Creates a card of the specified type.
     *
     * @param p_cardType The type of card to be created.
     * @return A new card instance of the specified type.
     */
    public static Card createCard(CardType p_cardType) {
        if (p_cardType == CardType.AIRLIFT) {
            return new AirliftCard();
        }
        if (p_cardType == CardType.BOMB) {
            return new BombCard();
        }
        if (p_cardType == CardType.BLOCKADE) {
            return new BlockadeCard();
        }
        if (p_cardType == CardType.DIPLOMACY) {
            return new DiplomacyCard();
        }
        return new EmptyCard();
    }
}

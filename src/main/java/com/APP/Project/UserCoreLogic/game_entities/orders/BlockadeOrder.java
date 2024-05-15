package com.APP.Project.UserCoreLogic.game_entities.orders;

import com.APP.Project.UserCoreLogic.constants.enums.CardType;
import com.APP.Project.UserCoreLogic.constants.enums.OrderTypes;
import com.APP.Project.UserCoreLogic.constants.interfaces.Card;
import com.APP.Project.UserCoreLogic.constants.interfaces.Order;
import com.APP.Project.UserCoreLogic.game_entities.Country;
import com.APP.Project.UserCoreLogic.game_entities.Player;
import com.APP.Project.UserCoreLogic.exceptions.CardNotFoundException;
import com.APP.Project.UserCoreLogic.exceptions.EntityNotFoundException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidGameException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidOrderException;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;
import com.APP.Project.UserCoreLogic.Container.CountryContainer;
import org.json.JSONObject;

import java.util.List;

/**
 * This class implements the Blockade card. When blockade card is
 * used then it simply multiplies the number of armies by certain constant value and make that country a neutral
 * country. A player can perform blockade operation on its own country.
 *
 * @author Sushant Sinha
 */
public class BlockadeOrder extends Order {
    private final Country d_targetCountry;
    /**
     * Constant to multiply the armies count.
     */
    public static final int CONSTANT = 3;

    /**
     * To find the country using its data members.
     */
    private final CountryContainer d_countryRepository = new CountryContainer();

    private final LogEntryBuffer d_logEntryBuffer = LogEntryBuffer.getLogger();

    /**
     * Sets the country name and current player object.
     *
     * @param p_targetCountry Country name.
     * @param p_owner         Player who has initiated this order.
     * @throws EntityNotFoundException Throws if the given country is not found in the list of available countries.
     */
    public BlockadeOrder(String p_targetCountry, Player p_owner)
            throws EntityNotFoundException {
        super(p_owner);
        d_targetCountry = d_countryRepository.findFirstByCountryName(p_targetCountry);
    }

    /**
     * Performs actual blockade operation.
     *
     * @throws InvalidOrderException If the order can not be performed due to an invalid country, an invalid number * of
     *                               armies, or other invalid input.
     * @throws CardNotFoundException Card doesn't found in the player's card list.
     */
    public void execute() throws InvalidOrderException, CardNotFoundException {
        Country l_country;
        List<Country> l_countryList;
        Card l_requiredCard;
        if (d_targetCountry.getOwnedBy().equals(this.getOwner())) {
            l_requiredCard = this.getOwner().getCard(CardType.BLOCKADE);
        } else {
            throw new InvalidOrderException("You have selected opponent player's country to perform blockade operation.");
        }

        l_country = d_targetCountry;
        l_countryList = this.getOwner().getAssignedCountries();
        try {
            l_country.setNumberOfArmies(l_country.getNumberOfArmies() * CONSTANT);
            l_countryList.remove(l_country);
        } catch (Exception e) {
            throw new InvalidOrderException("You can not perform blockade operation as you don't own this country");
        }
        this.getOwner().setAssignedCountries(l_countryList);
        this.getOwner().removeCard(l_requiredCard);

        // Logging
        StringBuilder l_logResponse = new StringBuilder();
        l_logResponse.append("Blockade card to triple the armies in " + d_targetCountry.getCountryName() + "\n");
        d_logEntryBuffer.dataChanged("blockade", l_logResponse.toString());
    }

    /**
     * Returns the type of order.
     *
     * @return Order type.
     */
    @Override
    public OrderTypes getType() {
        return OrderTypes.blockade;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void expire() {
        // Does nothing.
    }

    /**
     * Returns the string describing player order.
     *
     * @return String representing player orders.
     */
    @Override
    public String toString() {
        return String.format("%s %s", getType().getJsonValue(), d_targetCountry.getCountryName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJSON() {
        JSONObject l_order = new JSONObject();
        l_order.put("target", d_targetCountry.getCountryName());
        l_order.put("type", getType().name());
        return l_order;
    }

    /**
     * Creates an instance of this class and assigns the data members of the concrete class using the values inside
     * <code>JSONObject</code>.
     *
     * @param p_jsonObject <code>JSONObject</code> holding the runtime information.
     * @param p_player     Player who had issued this order.
     * @return Created instance of this class using the provided JSON data.
     * @throws InvalidGameException If the information from JSONObject cannot be used because it is corrupted or missing
     *                              the values.
     */
    public static BlockadeOrder fromJSON(JSONObject p_jsonObject, Player p_player) throws InvalidGameException {
        try {
            return new BlockadeOrder(p_jsonObject.getString("target"),
                    p_player);
        } catch (EntityNotFoundException p_entityNotFoundException) {
            throw new InvalidGameException();
        }
    }
}

package com.APP.Project.UserCoreLogic.game_entities.orders;

import com.APP.Project.UserCoreLogic.constants.enums.CardType;
import com.APP.Project.UserCoreLogic.constants.enums.OrderTypes;
import com.APP.Project.UserCoreLogic.constants.interfaces.Card;
import com.APP.Project.UserCoreLogic.constants.interfaces.Order;
import com.APP.Project.UserCoreLogic.game_entities.Player;
import com.APP.Project.UserCoreLogic.exceptions.CardNotFoundException;
import com.APP.Project.UserCoreLogic.exceptions.EntityNotFoundException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidGameException;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;
import com.APP.Project.UserCoreLogic.Container.PlayerContainer;
import org.json.JSONObject;


/**
 * This class implements the Diplomacy card.
 *
 * @author Sushant Sinha
 */
public class NegotiateOrder extends Order {
    private final Player d_otherPlayer;

    /**
     * To find the player using its data members.
     */
    private final PlayerContainer d_playerRepository = new PlayerContainer();

    private final LogEntryBuffer d_logEntryBuffer = LogEntryBuffer.getLogger();

    /**
     * Parameterised constructors
     *
     * @param p_thisPlayer  First player object.
     * @param p_otherPlayer Second player object.
     * @throws EntityNotFoundException Throws if the country with the given name doesn't exist.
     */
    public NegotiateOrder(Player p_thisPlayer, String p_otherPlayer) throws EntityNotFoundException {
        super(p_thisPlayer);
        d_otherPlayer = d_playerRepository.findByPlayerName(p_otherPlayer);
    }

    /**
     * Executes the method for adding player in each-others negotiation list.
     *
     * @throws CardNotFoundException Card doesn't found in the player's card list.
     */
    @Override
    public void execute() throws CardNotFoundException {
        StringBuilder l_logResponse = new StringBuilder();
        l_logResponse.append("\n" + "Executing " + this.getOwner().getName() + " Order:" + "\n");
        // Get diplomacy card.
        Card l_requiredCard = this.getOwner().getCard(CardType.DIPLOMACY);
        this.getOwner().addNegotiatePlayer(d_otherPlayer);
        d_otherPlayer.addNegotiatePlayer(this.getOwner());
        this.getOwner().removeCard(l_requiredCard);

        // Logging
        l_logResponse.append("\n Order Effect\n" + "Negotiating between " + this.getOwner().getName() + " and " + d_otherPlayer.getName() + "\n");
        d_logEntryBuffer.dataChanged("negotiate", l_logResponse.toString());
    }

    /**
     * Gets the type of order.
     *
     * @return Value of the order type.
     */
    public OrderTypes getType() {
        return OrderTypes.negotiate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void expire() {
        this.getOwner().removeNegotiatePlayer(d_otherPlayer);
        d_otherPlayer.removeNegotiatePlayer(this.getOwner());
    }

    /**
     * Returns the string describing player order.
     *
     * @return String representing player orders.
     */
    @Override
    public String toString() {
        return String.format("%s %s", getType().getJsonValue(), d_otherPlayer.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJSON() {
        JSONObject l_order = new JSONObject();
        l_order.put("other_player", d_otherPlayer.getName());
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
    public static NegotiateOrder fromJSON(JSONObject p_jsonObject, Player p_player) throws InvalidGameException {
        try {
            return new NegotiateOrder(p_player, p_jsonObject.getString("other_player"));
        } catch (EntityNotFoundException p_vmException) {
            throw new InvalidGameException();
        }
    }
}

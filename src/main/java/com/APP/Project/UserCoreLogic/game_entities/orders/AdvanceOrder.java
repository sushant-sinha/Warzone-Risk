package com.APP.Project.UserCoreLogic.game_entities.orders;

import com.APP.Project.UserCoreLogic.common.services.CardService;
import com.APP.Project.UserCoreLogic.constants.enums.OrderTypes;
import com.APP.Project.UserCoreLogic.constants.interfaces.Order;
import com.APP.Project.UserCoreLogic.game_entities.Country;
import com.APP.Project.UserCoreLogic.game_entities.Player;
import com.APP.Project.UserCoreLogic.exceptions.EntityNotFoundException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidArgumentException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidGameException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidOrderException;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;
import com.APP.Project.UserCoreLogic.Container.CountryContainer;
import com.jakewharton.fliptables.FlipTable;
import org.json.JSONObject;

import java.util.List;

import static java.lang.Math.round;

/**
 * This class implements operations performed when an advance order is executed.
 * Advance order moves the armies from the source country to the destination country.
 * <b>If the source and destination country is:</b>
 * <ul>
 *  <li>
 *     Owned by the same player then it simply moves armies and performs operations on the destination country.
 *  </li>
 *  <li>
 *      Not owned by the same player then the Battle will occur.
 *  </li>
 * </ul>
 *
 * @author Sushant Sinha
 */
public class AdvanceOrder extends Order {
    private final Country d_countryFrom;
    private final Country d_countryTo;
    private int d_numOfArmies;

    /**
     * For finding the country using its data members.
     */
    private final CountryContainer d_countryRepository = new CountryContainer();

    private final LogEntryBuffer d_logEntryBuffer = LogEntryBuffer.getLogger();

    /**
     * Sets the values of the data members.
     *
     * @param p_countryFrom Source country name.
     * @param p_countryTo   Destination country name.
     * @param p_numOfArmies Number of armies to be moved.
     * @param p_owner       Current player object.
     * @throws EntityNotFoundException  Throws if the country with the given name doesn't exist.
     * @throws InvalidArgumentException Throws if the input is invalid.
     */
    public AdvanceOrder(String p_countryFrom, String p_countryTo, String p_numOfArmies, Player p_owner)
            throws EntityNotFoundException, InvalidArgumentException {
        super(p_owner);
        d_countryFrom = d_countryRepository.findFirstByCountryName(p_countryFrom);
        d_countryTo = d_countryRepository.findFirstByCountryName(p_countryTo);
        try {
            d_numOfArmies = Integer.parseInt(p_numOfArmies);
            // Checks if the number of armies is less than zero (negative).
            if (d_numOfArmies < 0) {
                throw new InvalidArgumentException("Number of armies can not be negative.");
            }
        } catch (NumberFormatException p_e) {
            throw new InvalidArgumentException("Number of reinforcements is not a number.");
        }
    }

    /**
     * Performs the actual advance operation.
     *
     * @throws InvalidOrderException If the order can not be performed due to an invalid country, an invalid number of
     *                               armies, or other invalid input.
     */
    @Override
    public void execute() throws InvalidOrderException {
        StringBuilder l_logResponse = new StringBuilder();
        l_logResponse.append("\n" + "Executing " + this.getOwner().getName() + " Order:" + "\n");
        // Handles invalid country name (Country doesn't exists)
        List<Country> l_assignCountryList = this.getOwner().getAssignedCountries();
        List<Country> l_neighborCountryList = d_countryFrom.getNeighbourCountries();

        // Checks the source country is owned by a current player or not. If not then throws an exception.
        if (!l_assignCountryList.contains(d_countryFrom)) {
            throw new InvalidOrderException("Please select your own country as a source country.");
        }

        // Checks whether the destination country is the neighbor country of the source country or not. If not then throws an exception.
        if (!l_neighborCountryList.contains(d_countryTo)) {
            throw new InvalidOrderException("Please select any of the neighbor country of the source country as a destination country as we can perform Advance order on neighbor countries only.");
        }
        if (this.getOwner().isNotNegotiation(d_countryTo.getOwnedBy())) {
            // If destination country is owned by the current player then it simply moves armies to the destination country.
            if (l_assignCountryList.contains(d_countryTo)) {
                //move armies and add
                int l_remainingArmies = d_countryFrom.getNumberOfArmies() - d_numOfArmies;
                if (l_remainingArmies < 0) {
                    //throw new InvalidInputException("Insufficient armies");
                    d_numOfArmies = d_countryFrom.getNumberOfArmies();
                    l_remainingArmies = 0;
                }
                l_logResponse.append(this.getOwner().getName() + " moved " + d_numOfArmies + " armies from " + d_countryFrom.getCountryName() + " to " + d_countryTo.getCountryName());
                d_countryFrom.setNumberOfArmies(l_remainingArmies);
                d_countryTo.setNumberOfArmies(d_countryTo.getNumberOfArmies() + d_numOfArmies);

                // Logging
                String[] l_header = {"COUNTRY", "ARMY COUNT"};
                String[][] l_changeContent = {
                        {d_countryTo.getCountryName(), String.valueOf(d_countryTo.getNumberOfArmies())},
                        {d_countryFrom.getCountryName(), String.valueOf(d_countryFrom.getNumberOfArmies())}
                };
                l_logResponse.append("\n Order Effect\n" + FlipTable.of(l_header, l_changeContent));
                d_logEntryBuffer.dataChanged("advance", l_logResponse.toString());
            }
            // If destination country is not owned by the current player than it performs battle.
            else {
                // Move armies and battle
                int l_defendingArmies = d_countryTo.getNumberOfArmies();
                int l_attackingArmies = d_numOfArmies;
                int l_remainingArmies = d_countryFrom.getNumberOfArmies() - d_numOfArmies;
                if (l_remainingArmies < 0) {
                    l_attackingArmies = d_countryFrom.getNumberOfArmies();
                    l_remainingArmies = 0;
                }
                d_countryFrom.setNumberOfArmies(l_remainingArmies);

                int l_attackersKilled = (int) round(l_defendingArmies * 0.7);
                int l_defendersKilled = (int) round(l_attackingArmies * 0.6);

                if (d_countryFrom.getNumberOfArmies() <= 0) {
                    throw new InvalidOrderException("Country doesn't have armies to advance!");
                }

                if (l_defendersKilled >= l_defendingArmies) {
                    Player l_countryToOwner = d_countryTo.getOwnedBy();
                    l_countryToOwner.removeCountry(d_countryTo);

                    // Owner changed.
                    d_countryTo.setOwnedBy(this.getOwner());
                    List<Country> l_assign = this.getOwner().getAssignedCountries();
                    l_assign.add(d_countryTo);
                    this.getOwner().setAssignedCountries(l_assign);
                    d_countryTo.setNumberOfArmies(l_attackingArmies - l_attackersKilled);

                    this.getOwner().addCard(CardService.randomCard());
                    l_logResponse.append(l_countryToOwner.getName() + " won the attack!!!!\n" + l_countryToOwner.getName() + " moved " + l_attackingArmies + " armies from " + d_countryFrom.getCountryName() + " to attack on " + d_countryTo.getCountryName());
                    String[] l_header = {"COUNTRY", "ARMY COUNT", "PREVIOUS OWNER", "NEW OWNER"};
                    String[][] l_changeContent = {
                            {d_countryTo.getCountryName(), String.valueOf(d_countryTo.getNumberOfArmies()), l_countryToOwner.getName(), this.getOwner().getName()}
                    };
                    l_logResponse.append("\n Order Effect\n" + FlipTable.of(l_header, l_changeContent));
                } else {
                    d_countryFrom.setNumberOfArmies(d_countryFrom.getNumberOfArmies() + l_attackingArmies - l_attackersKilled);
                    d_countryTo.setNumberOfArmies(l_defendingArmies - l_defendersKilled);

                    // Logging
                    l_logResponse.append(this.getOwner().getName() + " did not won the attack!!!!\n" + this.getOwner().getName() + " moved " + l_attackingArmies + " armies from " + d_countryFrom.getCountryName() + " to attack on " + d_countryTo.getCountryName());
                    String[] l_header = {"COUNTRY", "ARMY COUNT"};
                    String[][] l_changeContent = {
                            {d_countryTo.getCountryName(), String.valueOf(d_countryTo.getNumberOfArmies())},
                            {d_countryFrom.getCountryName(), String.valueOf(d_countryFrom.getNumberOfArmies())}
                    };
                    l_logResponse.append("\n Order Effect\n" + FlipTable.of(l_header, l_changeContent));
                }
                d_logEntryBuffer.dataChanged("advance", l_logResponse.toString());
            }
        }
    }

    /**
     * Returns the type of order.
     *
     * @return Order type.
     */
    @Override
    public OrderTypes getType() {
        return OrderTypes.advance;
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
        return String.format("%s %s %s %s", getType().getJsonValue(), d_countryFrom.getCountryName(), d_countryTo.getCountryName(), d_numOfArmies);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJSON() {
        JSONObject l_order = new JSONObject();
        l_order.put("source", d_countryFrom.getCountryName());
        l_order.put("target", d_countryTo.getCountryName());
        l_order.put("numOfArmies", d_numOfArmies);
        l_order.put("type", getType().name());
        return l_order;
    }

    /**
     * Creates an instance of this class. Assigns the data members of the concrete class using the values inside
     * <code>JSONObject</code>.
     *
     * @param p_jsonObject <code>JSONObject</code> holding the runtime information.
     * @param p_player     Player who had issued this order.
     * @return Created instance of this class using the provided JSON data.
     * @throws InvalidGameException If the information from JSONObject cannot be used because it is corrupted or missing
     *                              the values.
     */
    public static AdvanceOrder fromJSON(JSONObject p_jsonObject, Player p_player) throws InvalidGameException {
        try {
            return new AdvanceOrder(p_jsonObject.getString("source"),
                    p_jsonObject.getString("target"),
                    String.valueOf(p_jsonObject.getInt("numOfArmies")),
                    p_player);
        } catch (EntityNotFoundException | InvalidArgumentException p_entityNotFoundException) {
            throw new InvalidGameException();
        }
    }
}
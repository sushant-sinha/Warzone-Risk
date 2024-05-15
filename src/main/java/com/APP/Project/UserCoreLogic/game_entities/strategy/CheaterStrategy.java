package com.APP.Project.UserCoreLogic.game_entities.strategy;

import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.constants.enums.StrategyType;
import com.APP.Project.UserCoreLogic.game_entities.Country;
import com.APP.Project.UserCoreLogic.game_entities.Player;
import com.APP.Project.UserCoreLogic.game_entities.orders.DeployOrder;
import com.APP.Project.UserCoreLogic.exceptions.EntityNotFoundException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidArgumentException;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * This class defines the behavior of Cheater player.
 *
 * @author Sushant Sinha
 */
public class CheaterStrategy extends PlayerStrategy {
    private final LogEntryBuffer d_logEntryBuffer = LogEntryBuffer.getLogger();

    /**
     * Parameterised constructor
     *
     * @param p_player Player of this strategy.
     */
    public CheaterStrategy(Player p_player) {
        super(p_player);
    }

    /**
     * This method is for transfering the ownership of all the neighbour enemy countries to the cheater player.
     */
    public void doesCheat() {
        List<Country> l_futureOwningCountryList = new ArrayList<>();
        // Add countries to the cheater player
        for (Country l_traverseCountry : d_player.getAssignedCountries()) {
            for (Country l_neighbourCountry : l_traverseCountry.getNeighbourCountries()) {
                if (!l_neighbourCountry.getOwnedBy().equals(d_player)) {
                    l_futureOwningCountryList.add(l_neighbourCountry);
                }
            }
        }
        for (Country l_futureOwningCountry : l_futureOwningCountryList) {
            l_futureOwningCountry.getOwnedBy().removeCountry(l_futureOwningCountry);
        }
        for (Country l_futureOwningCountry : l_futureOwningCountryList) {
            l_futureOwningCountry.setOwnedBy(d_player);
            d_player.addAssignedCountries(l_futureOwningCountry);
        }
    }

    /**
     * This function doubles the cheater player's army count
     */
    public void doubleArmies() {
        for (Country l_traverseCountry : d_player.getAssignedCountries()) {
            for (Country l_neighbourCountry : l_traverseCountry.getNeighbourCountries()) {
                if (!l_neighbourCountry.getOwnedBy().equals(d_player)) {
                    l_traverseCountry.setNumberOfArmies(l_traverseCountry.getNumberOfArmies() * 2);
                    break;
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() throws EntityNotFoundException, InvalidArgumentException {
        List<Country> l_ownedCountries = d_player.getAssignedCountries();
        if (UserCoreLogic.getGameEngine().isTournamentModeOn() && d_player.getRemainingReinforcementCount() > 0) {
            // If the player has less armies than the number of assigned countries.
            if (d_player.getAssignedCountries().size() > d_player.getRemainingReinforcementCount()) {
                for (int i = 0; i < d_player.getRemainingReinforcementCount(); i++) {
                    DeployOrder l_deployOrder = new DeployOrder(l_ownedCountries.get(i).getCountryName(), String.valueOf(1), d_player);
                    this.d_player.addOrder(l_deployOrder);
                }
            } else {
                // If the player has more armies than the number of assigned countries.
                int l_remainingReinforcementCount = d_player.getRemainingReinforcementCount();
                int l_assignReinforcementCount = d_player.getRemainingReinforcementCount() / d_player.getAssignedCountries().size();
                for (int i = 0; i < d_player.getAssignedCountries().size() - 1; i++) {
                    DeployOrder l_deployOrder = new DeployOrder(l_ownedCountries.get(i).getCountryName(), String.valueOf(l_assignReinforcementCount), d_player);
                    this.d_player.addOrder(l_deployOrder);
                    l_remainingReinforcementCount -= l_assignReinforcementCount;
                }
                DeployOrder l_deployOrder = new DeployOrder(l_ownedCountries.get(l_ownedCountries.size() - 1).getCountryName(), String.valueOf(l_remainingReinforcementCount), d_player);
                this.d_player.addOrder(l_deployOrder);
            }
        }
        doesCheat();
        doubleArmies();
        d_logEntryBuffer.dataChanged("issue_order", String.format("%s player's turn to Issue Order", this.d_player.getName()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StrategyType getType() {
        return StrategyType.CHEATER;
    }
}

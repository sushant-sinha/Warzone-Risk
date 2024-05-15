package com.APP.Project.UserCoreLogic.game_entities.strategy;

import com.APP.Project.UserCoreLogic.constants.enums.StrategyType;
import com.APP.Project.UserCoreLogic.game_entities.Country;
import com.APP.Project.UserCoreLogic.game_entities.Player;
import com.APP.Project.UserCoreLogic.game_entities.orders.DeployOrder;
import com.APP.Project.UserCoreLogic.exceptions.EntityNotFoundException;
import com.APP.Project.UserCoreLogic.exceptions.InvalidArgumentException;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;

import java.util.List;

/**
 * This class defines the behavior of benevolent player.
 *
 * @author Sushant Sinha
 */
public class BenevolentStrategy extends PlayerStrategy {
    private final LogEntryBuffer d_logEntryBuffer = LogEntryBuffer.getLogger();

    /**
     * Parameterised constructor to set the player.
     *
     * @param p_player Player of this strategy.
     */
    public BenevolentStrategy(Player p_player) {
        super(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() throws InvalidArgumentException, EntityNotFoundException {
        List<Country> l_ownedCountries = d_player.getAssignedCountries();

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
            d_logEntryBuffer.dataChanged("issue_order", String.format("%s player's turn to Issue Order", this.d_player.getName()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StrategyType getType() {
        return StrategyType.BENEVOLENT;
    }
}

package fr.antoinerochas.cerebrum.user;

import fr.antoinerochas.cerebrum.Cerebrum;
import fr.antoinerochas.cerebrum.order.Order;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

/**
 * This file is part of Cerebrum.
 *
 * @author Aro at/on 27/02/2020
 * @since 1.0
 */

public record CerebrumUser(String id, int language, ArrayList<Order> orders, long lastOrdered)
{
    /**
     * Tells if a {@link User} is a {@code Cerebrum}'s operator.
     *
     * @return {@link Boolean#TRUE} if it is, {@link Boolean#FALSE} otherwise
     */
    public boolean isOperator()
    {
        return Cerebrum.getConfigManager().getOperatorsIds().contains(id) || id.equals(Cerebrum.OWNER);
    }

    public User getUser() { return Cerebrum.getJDA().getUserById(id); }

    @Override
    public String toString()
    {
        return "CerebrumUser{" +
                "id='" + id + '\'' +
                ", language=" + language +
                ", orders=" + orders +
                ", lastOrdered=" + lastOrdered +
                '}';
    }
}

package fr.antoinerochas.cerebrum.user;

import fr.antoinerochas.cerebrum.Cerebrum;
import fr.antoinerochas.cerebrum.i18n.Language;
import fr.antoinerochas.cerebrum.order.Order;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

/**
 * This file is part of Cerebrum.
 *
 * @author Aro at/on 27/02/2020
 * @since 1.0
 */
public class CerebrumUser
{
    /**
     * Represent user's Discord id.
     */
    private String id;

    /**
     * Represent user's preferred language.
     */
    private int language;

    /**
     * Represent user's orders.
     */
    private ArrayList<Order> orders;

    /**
     * Represent user's last order timestamp.
     */
    private long lastOrdered;

    public CerebrumUser(String id, int language, ArrayList<Order> orders, long lastOrdered)
    {
        this.id = id;
        this.language = language;
        this.orders = orders;
        this.lastOrdered = lastOrdered;
    }

    /**
     * Tells if a {@link User} is a {@code Cerebrum}'s operator.
     *
     * @return {@link Boolean#TRUE} if it is, {@link Boolean#FALSE} otherwise
     */
    public boolean isOperator()
    {
        return Cerebrum.getConfigManager().getOperatorsIds().contains(id) || getId().equals(Cerebrum.OWNER);
    }

    public Language getUserLanguage()
    {
        return Language.values()[language];
    }

    public int getLanguage()
    {
        return language;
    }

    public void setLanguage(int language)
    {
        this.language = language;
        refresh();
    }

    public long getLastOrdered()
    {
        return lastOrdered;
    }

    public void setLastOrdered(long lastOrdered)
    {
        this.lastOrdered = lastOrdered;
        refresh();
    }

    public ArrayList<Order> getOrders()
    {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders)
    {
        this.orders = orders;
        refresh();
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public User getUser() { return Cerebrum.getJDA().getUserById(id); }

    private void refresh()
    {
        Cerebrum.getUserManager().refreshUser(this);
    }

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

package fr.antoinerochas.cerebrum.user;

import fr.antoinerochas.cerebrum.i18n.Language;
import fr.antoinerochas.cerebrum.order.Order;

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
     * Represent user's preferred language.
     */
    private int language;

    /**
     * Represent user's last order timestamp.
     */
    private long lastOrdered;

    /**
     * Represent user's orders.
     */
    private final ArrayList<Order> orders;

    /**
     * Represent user's Discord id.
     */
    private transient final String id;

    public CerebrumUser(int language, ArrayList<Order> orders, long lastOrdered, String id)
    {
        this.language = language;
        this.orders = orders;
        this.lastOrdered = lastOrdered;
        this.id = id;
    }

    public int getLanguage()
    {
        return language;
    }

    public Language getUserLanguage()
    {
        return Language.values()[language];
    }

    public void setLanguage(int language)
    {
        this.language = language;
    }

    public long getLastOrdered()
    {
        return lastOrdered;
    }

    public void setLastOrdered(long lastOrdered)
    {
        this.lastOrdered = lastOrdered;
    }

    public ArrayList<Order> getOrders()
    {
        return orders;
    }

    public String getId()
    {
        return id;
    }
}

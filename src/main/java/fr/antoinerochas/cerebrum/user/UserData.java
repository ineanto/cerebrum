package fr.antoinerochas.cerebrum.user;

import fr.antoinerochas.cerebrum.i18n.Language;
import fr.antoinerochas.cerebrum.order.Order;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

/**
 * This file is part of Cerebrum.
 * Represent {@link User}'s data.
 *
 * @author Aro at/on 01/02/2020
 * @since 1.0
 */
public class UserData
{
    /**
     * Represent user's preferred language.
     */
    private int language;

    /**
     * Represent user's orders.
     */
    private ArrayList<Order> orderList;

    /**
     * Constructor.
     *
     * @param language  user's preferred language
     * @param orderList user's orders
     */
    public UserData(int language, ArrayList<Order> orderList)
    {
        this.language = language;
        this.orderList = orderList;
    }

    /**
     * Get user's preferred language.
     *
     * @return user's preferred language as {@link Language}
     */
    public Language getLanguage()
    {
        return Language.values()[language];
    }

    /**
     * Get user's order list.
     *
     * @return user's order list
     */
    public ArrayList<Order> getOrderList()
    {
        return orderList;
    }
}

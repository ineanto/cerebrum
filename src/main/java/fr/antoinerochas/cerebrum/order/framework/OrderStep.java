package fr.antoinerochas.cerebrum.order.framework;

import fr.antoinerochas.cerebrum.order.Order;
import fr.antoinerochas.cerebrum.user.CerebrumUser;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * This file is part of Cerebrum.
 * Represent an {@link Order}'s step.
 *
 * @author aro on 17/03/2020
 * @since 1.0
 */
public abstract class OrderStep
{
    /**
     * The message.
     */
    private Message message;

    /**
     * The channel.
     */
    private final TextChannel channel;

    /**
     * The order.
     */
    private final Order order;

    /**
     * The user.
     */
    private final CerebrumUser user;

    /**
     * Constructor.
     *
     * @param channel the channel
     * @param order   the order
     * @param user    the user
     */
    public OrderStep(TextChannel channel, Order order, CerebrumUser user)
    {
        this.channel = channel;
        this.order = order;
        this.user = user;
    }

    /**
     * Represents the step's name.
     *
     * @return the step's name
     */
    public abstract String name();

    /**
     * Called before the step processing.
     *
     * @param message the message
     * @param order   the order
     * @param user    the user
     * @see #process(Message, Order, CerebrumUser)
     * @see #after(Message, Order, CerebrumUser)
     */
    public abstract void before(Message message, Order order, CerebrumUser user);

    /**
     * Process the step.
     *
     * @param message the message
     * @param order   the order
     * @param user    the user
     * @see #before(Message, Order, CerebrumUser)
     * @see #after(Message, Order, CerebrumUser)
     */
    public abstract void process(Message message, Order order, CerebrumUser user);

    /**
     * Called after the step processing.
     *
     * @param message the message
     * @param order   the order
     * @param user    the user
     * @see #before(Message, Order, CerebrumUser)
     * @see #process(Message, Order, CerebrumUser)
     */
    public abstract void after(Message message, Order order, CerebrumUser user);

    /**
     * Called if something fails.
     *
     * @param order the order
     * @param user  the user
     */
    public abstract void panic(Order order, CerebrumUser user);

    /**
     * Performs a basic check.
     *
     * @return if the check passed
     */
    public boolean check()
    {
        return user != null && order != null;
    }

    public Message getMessage()
    {
        return message;
    }

    public TextChannel getChannel()
    {
        return channel;
    }

    public void setMessage(Message message)
    {
        this.message = message;
    }

    public Order getOrder()
    {
        return order;
    }

    public CerebrumUser getUser()
    {
        return user;
    }
}

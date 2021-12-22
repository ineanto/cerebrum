package fr.antoinerochas.cerebrum.order.framework;

import fr.antoinerochas.cerebrum.embed.EmbedMaker;
import fr.antoinerochas.cerebrum.i18n.Messages;
import fr.antoinerochas.cerebrum.i18n.I18N;
import fr.antoinerochas.cerebrum.i18n.Language;
import fr.antoinerochas.cerebrum.order.Order;
import fr.antoinerochas.cerebrum.user.CerebrumUser;
import fr.antoinerochas.cerebrum.utils.Color;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

/**
 * This file is part of Cerebrum.
 * Represent an {@link Order}'s step.
 *
 * @author aro on 17/03/2020
 * @since 1.0
 */
public abstract class Step
{
    /**
     * The channel.
     */
    private final MessageChannel channel;

    /**
     * The order.
     */
    private final Order order;

    /**
     * The user.
     */
    private final CerebrumUser user;

    /**
     * The message.
     */
    private Message message;

    /**
     * Is this step the last one ?
     */
    private boolean last;

    /**
     * Constructor.
     *
     * @param channel the channel
     * @param order   the order
     * @param user    the user
     */
    public Step(MessageChannel channel, Order order, CerebrumUser user)
    {
        this.channel = channel;
        this.order = order;
        this.user = user;
        this.last = false;
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
     * @param order the order
     * @param user  the user
     * @see #process(Order, CerebrumUser)
     * @see #after(Order, CerebrumUser)
     */
    public abstract void before(Order order, CerebrumUser user);

    /**
     * Process the step.
     *
     * @param order the order
     * @param user  the user
     * @see #before(Order, CerebrumUser)
     * @see #after(Order, CerebrumUser)
     */
    public abstract void process(Order order, CerebrumUser user);

    /**
     * Called after the step processing.
     *
     * @param order the order
     * @param user  the user
     * @see #before(Order, CerebrumUser)
     * @see #process(Order, CerebrumUser)
     */
    public abstract void after(Order order, CerebrumUser user);

    /**
     * Called if something fails.
     *
     * @param order the order
     * @param user  the user
     */
    public void panic(Order order, CerebrumUser user)
    {
        EmbedMaker.make(Color.RED, I18N.get(user.getUserLanguage(), Messages.Global.ERROR), I18N.get(user.getUserLanguage(), Messages.Global.ERROR_DESC));
    }

    /**
     * Performs a basic check.
     *
     * @return if the check passed
     */
    public boolean check()
    {
        return user != null && order != null;
    }

    /**
     * Tell if the {@link Step} is the last one.
     *
     * @return {@link Boolean#TRUE} if it is, {@link Boolean#FALSE} otherwise.
     */
    public boolean isLast()
    {
        return last;
    }

    /**
     * Defines this {@link Step} as the last one.
     */
    public void setLast() { this.last = true; }

    public Message getMessage()
    {
        return message;
    }

    public MessageChannel getChannel()
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

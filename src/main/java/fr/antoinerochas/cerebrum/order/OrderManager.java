package fr.antoinerochas.cerebrum.order;

import fr.antoinerochas.cerebrum.Cerebrum;
import fr.antoinerochas.cerebrum.i18n.I18NManager;
import fr.antoinerochas.cerebrum.user.CerebrumUser;
import fr.antoinerochas.cerebrum.utils.Color;
import fr.antoinerochas.cerebrum.utils.EmbedMaker;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * This file is part of Cerebrum.
 * Is responsible for taking orders from customers.
 *
 * @author Aro at/on 28/01/2020
 * @since 1.0
 */
public class OrderManager
{
    /**
     * Log4J's {@link Logger} instance.
     */
    public static final Logger LOGGER = LogManager.getLogger(OrderManager.class);

    /**
     * Represents the default {@link Order}.
     */
    public static final Order DEFAULT_ORDER = new Order(I18NManager.getValue("orderNullValue"), OrderType.OTHER, I18NManager.getValue("orderNullValue"), -1, -1, -1);

    /**
     * The {@link JDA} instance.
     */
    private JDA jda;

    /**
     * Represents all the ongoing orders.
     */
    private HashMap<String, Order> ongoingOrders = new HashMap<>();

    /**
     * The current {@link OrderManager}'s status.
     */
    private OrderStatus status = OrderStatus.OFF;

    /**
     * Constructor.
     *
     * @param jda the current {@link JDA} instance
     */
    public OrderManager(JDA jda)
    {
        LOGGER.debug("Loading OrderManger...");
        // Define JDA instance.
        this.jda = jda;
        // If we're under Debug Mode, set the status in consequence.
        if (Cerebrum.DEBUG) { this.setStatus(OrderStatus.DEBUG); return; }
        //Else, set OrderManager under the default status.
        this.setStatus(OrderStatus.AVAILABLE);
    }

    /**
     * Start the order process from a {@link User}.
     *
     * @param user the user that took an {@link Order}
     */
    public void startOrderProcess(User user)
    {
        // TODO: 27/02/2020 Finish eventually...

        final CerebrumUser cerebrumUser = Cerebrum.getUserManager().getUser(user);
        final PrivateChannel channel = user.openPrivateChannel().complete();

        // If orders are disabled, tell the user.
        if (status != OrderStatus.AVAILABLE)
        {
            if (!cerebrumUser.isOperator())
            {
                MessageEmbed.Field orderUnavailable = new MessageEmbed.Field("Your order can't be processed.", I18NManager.getValue(cerebrumUser.getUserLanguage(), "orderUnavailable"), true);
                MessageEmbed error = EmbedMaker.make(Color.RED, "Sorry " + user.getName() + "...", null, orderUnavailable);
                channel.sendMessage(error).complete();
                channel.close().complete();
                return;
            }
            else
            {
                LOGGER.debug("User " + user.getId() + " has bypassed OMS.");
            }
        }

        LOGGER.info("Taking order from " + user.getName() + "(" + user.getId() + ")...");

        final Order order = DEFAULT_ORDER.clone();
        order.setCustomerId(user.getId());

        if (Cerebrum.DEBUG) { LOGGER.debug(order.toString()); }

        order.processStep(channel, cerebrumUser);
        channel.close().complete();
    }

    /**
     * Set the current {@link OrderManager}'s status.
     *
     * @param status the new status {@link OrderManager} will be in
     */
    public void setStatus(OrderStatus status)
    {
        this.status = status;
        // Print the status change.
        LOGGER.info("OrderManager Status has been switched to " + status.name() + ". Applying changes...");
    }

    /**
     * Get the current {@link OrderManager}'s status.
     *
     * @return the current {@link OrderManager}'s {@link OrderStatus}
     */
    public OrderStatus getStatus()
    {
        return status;
    }
}

package fr.antoinerochas.cerebrum.order;

import fr.antoinerochas.cerebrum.Cerebrum;
import fr.antoinerochas.cerebrum.embed.ComplexEmbed;
import fr.antoinerochas.cerebrum.i18n.I18N;
import fr.antoinerochas.cerebrum.i18n.Messages;
import fr.antoinerochas.cerebrum.order.framework.StepManager;
import fr.antoinerochas.cerebrum.user.CerebrumUser;
import fr.antoinerochas.cerebrum.utils.Color;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

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
    public static final Order DEFAULT_ORDER = new Order(I18N.get(Messages.Global.NULL), OrderType.OTHER, I18N.get(Messages.Global.NULL), -1, -1, -1);

    /**
     * The {@link JDA} instance.
     */
    private JDA jda;

    /**
     * The {@link StepManager} instance.
     */
    private final StepManager stepManager = new StepManager();

    /**
     * Represents all the ongoing orders.
     */
    private ArrayList<String> ongoingOrders = new ArrayList<>();

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
        final String id = user.getId();

        final ComplexEmbed errorEmbed = new ComplexEmbed(channel, cerebrumUser);
        errorEmbed.setColor(Color.RED);
        errorEmbed.setTitle(Messages.Global.ERROR);
        errorEmbed.setDescription(Messages.Global.ERROR_DESC);

        // If orders are disabled, tell the user.
        if (status != OrderStatus.AVAILABLE)
        {
            if (!cerebrumUser.isOperator())
            {
                errorEmbed.setMessage(Messages.Order.NOT_AVAILABLE);
                errorEmbed.send();
                return;
            }

            LOGGER.debug("User " + id + " has bypassed order manager's status.");
        }

        if (ongoingOrders.contains(id))
        {
            errorEmbed.setMessage(Messages.Order.ALREADY_ORDERING);
            errorEmbed.send();
            return;
        }

        LOGGER.info("Taking order from " + user.getName() + "(" + id + ")...");

        // Clone the default order and modify some fields.
        final Order order = DEFAULT_ORDER.clone();
        order.setCustomerId(id);
        order.start(channel, cerebrumUser);
        ongoingOrders.add(id);
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

    /**
     * Get {@link StepManager}'s instance.
     *
     * @return {@link StepManager}'s instance
     */
    public StepManager getStepManager()
    {
        return stepManager;
    }
}

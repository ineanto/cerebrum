package fr.antoinerochas.cerebrum.order;

import fr.antoinerochas.cerebrum.Cerebrum;
import fr.antoinerochas.cerebrum.user.CerebrumUser;
import fr.antoinerochas.cerebrum.utils.EventWaiter;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * This file is part of Cerebrum.
 * Represent an Order.
 *
 * @author Aro at/on 30/01/2020
 * @since 1.0
 */
public class Order implements Cloneable
{
    /**
     * Log4J's {@link Logger} instance.
     */
    public static final transient Logger LOGGER = LogManager.getLogger(Order.class);

    /**
     * Customer's ID.
     */
    private String customerId;

    /**
     * Order's description.
     */
    private OrderType type;

    /**
     * Order's description.
     */
    private String description;

    /**
     * Order's price.
     */
    private int price;

    /**
     * Order's date of order.
     */
    private long ordered;

    /**
     * Order's deadline.
     */
    private long deadline;

    /**
     * Order's step.
     */
    private transient OrderStep step;

    /**
     * Represents if an order is done.
     */
    private transient boolean done;

    /**
     * Constructor.
     *
     * @param customerId  customer's ID
     * @param type        order's type
     * @param description order's description
     * @param price       order's price (in EUR)
     * @param ordered     order's date of order
     * @param deadline    order's deadline
     */
    public Order(String customerId, OrderType type, String description, int price, long ordered, long deadline)
    {
        // Define parameters.
        this.customerId = customerId;
        this.type = type;
        this.description = description;
        this.price = price;
        this.ordered = ordered;
        this.deadline = deadline;
        this.step = OrderStep.TYPE;
    }

    /**
     * Process the next Order's step.
     *
     * @param channel      the channel
     * @param cerebrumUser the user
     */
    public void processStep(PrivateChannel channel, CerebrumUser cerebrumUser)
    {
        final EventWaiter eventWaiter = Cerebrum.getEventWaiter();

        switch (step)
        {
            case TYPE:
                eventWaiter.waitForEvent(MessageReceivedEvent.class, e -> e.getAuthor().equals(cerebrumUser.getUser())
                                && e.getChannel().equals(channel)
                                && e.getMessage().getIdLong() != channel.getLatestMessageIdLong(),
                        e ->
                        {

                        }, 1, TimeUnit.MINUTES, () -> channel.sendMessage("LOL").queue()
                );
                break;
            case DESCRIPTION:
                break;
            case PRICE:
                break;
            case DEADLINE:
                break;
            case DONE:
                break;
        }
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }

    public OrderType getType()
    {
        return type;
    }

    public void setType(OrderType type)
    {
        this.type = type;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public long getOrdered()
    {
        return ordered;
    }

    public void setOrdered(long ordered)
    {
        this.ordered = ordered;
    }

    public long getDeadline()
    {
        return deadline;
    }

    public void setDeadline(long deadline)
    {
        this.deadline = deadline;
    }

    public OrderStep getStep()
    {
        return step;
    }

    public void progressStep()
    {
        setStep(getNextStep());
    }

    public OrderStep getNextStep()
    {
        OrderStep step = OrderStep.values()[getStep().ordinal() + 1];
        return step == null ? OrderStep.DONE : step;
    }

    public void setStep(OrderStep step)
    {
        this.step = step;
    }

    public boolean isDone()
    {
        return step == OrderStep.DONE;
    }

    @Override
    public String toString()
    {
        return "Order{" +
                "customerId='" + customerId + '\'' +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", ordered=" + ordered +
                ", deadline=" + deadline +
                ", step=" + step +
                ", done=" + done +
                '}';
    }

    @Override
    public Order clone()
    {
        try
        {
            return (Order) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            LOGGER.error("Failed to clone the Order!", e);
            return null;
        }
    }
}

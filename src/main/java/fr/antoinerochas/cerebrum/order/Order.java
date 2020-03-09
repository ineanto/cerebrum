package fr.antoinerochas.cerebrum.order;

import fr.antoinerochas.cerebrum.Cerebrum;
import fr.antoinerochas.cerebrum.i18n.I18NManager;
import fr.antoinerochas.cerebrum.jda.api.ReactionListener;
import fr.antoinerochas.cerebrum.user.CerebrumUser;
import fr.antoinerochas.cerebrum.utils.Color;
import fr.antoinerochas.cerebrum.utils.EmbedMaker;
import fr.antoinerochas.cerebrum.utils.EventWaiter;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

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
        final AtomicBoolean pass = new AtomicBoolean(false);

        // wtf
        // not very optimized, rework later
        // but i don't think i can make something better for now
        switch (step)
        {
            case TYPE:
                sendOrderUpdate(channel, Color.ORANGE, cerebrumUser, "orderTypeSetTitle", "orderTypeSetDesc", "orderTypeSetMsg", this, null, message ->
                {
                    ReactionListener<String> handler = new ReactionListener<>(cerebrumUser.getUser().getIdLong(), message.getId());
                    // Speaker Emoji -> Discord
                    handler.registerReaction("\ud83d\udd0a", (ret) -> sendOrderUpdate(channel, Color.GREEN, cerebrumUser, "opSuccess", "orderTypeUpdatedDesc", "orderTypeUpdatedMsg", this, order ->
                    {
                        order.setType(OrderType.DISCORD);
                        processNextStep(channel, cerebrumUser);
                        handler.disable();
                    }, null, OrderType.DISCORD.name()));
                    // Controller Emoji -> Minecraft
                    handler.registerReaction("\ud83c\udfae", (ret) -> sendOrderUpdate(channel, Color.GREEN, cerebrumUser, "opSuccess", "orderTypeUpdatedDesc", "orderTypeUpdatedMsg", this, order ->
                    {
                        order.setType(OrderType.MINECRAFT);
                        processNextStep(channel, cerebrumUser);
                        handler.disable();
                    }, null, OrderType.MINECRAFT.name()));
                    // Computer Emoji -> Application
                    // TODO: 09/03/2020 Apparently clicking on this emoji does nothing and crash the order process ?
                    handler.registerReaction("\uD83D\uDDA5️", (ret) -> sendOrderUpdate(channel, Color.GREEN, cerebrumUser, "opSuccess", "orderTypeUpdatedDesc", "orderTypeUpdatedMsg", this, order ->
                    {
                        order.setType(OrderType.APPLICATION);
                        processNextStep(channel, cerebrumUser);
                        handler.disable();
                    }, null, OrderType.APPLICATION.name()));
                    // Question Mark Emoji -> Other
                    handler.registerReaction("\u2753", (ret) -> sendOrderUpdate(channel, Color.GREEN, cerebrumUser, "opSuccess", "orderTypeUpdatedDesc", "orderTypeUpdatedMsg", this, order ->
                    {
                        order.setType(OrderType.OTHER);
                        processNextStep(channel, cerebrumUser);
                        handler.disable();
                    }, null, OrderType.OTHER.name()));

                    Cerebrum.getReactionManager().addReactionListener(Cerebrum.GUILD.getIdLong(), message, handler);
                });
                break;
            case DESCRIPTION:
                sendOrderUpdate(channel, Color.ORANGE, cerebrumUser, "orderDescSetTitle", "orderDescSetDesc", "orderDescSetMsg", this, null, null);
                eventWaiter.waitForEvent(MessageReceivedEvent.class,
                        event ->
                                event.getMessage().getIdLong() != channel.getLatestMessageIdLong() &&
                                        !event.getAuthor().isBot() &&
                                        event.isFromType(ChannelType.PRIVATE) &&
                                        !event.isFromGuild(),
                        event ->
                        {
                            final String description = event.getMessage().getContentRaw();
                            sendOrderUpdate(channel, Color.GREEN, cerebrumUser, "opSuccess", "orderMadeProgress", "orderDescUpdatedMsg", this, order ->
                                    order.setDescription(description), null);
                            processNextStep(channel, cerebrumUser);
                        }, 5, TimeUnit.MINUTES, () ->
                                // TODO: 09/03/2020 Cancel order
                                sendOrderUpdate(channel, Color.RED, cerebrumUser, "FAIL", null, null, this, order ->
                                        order.setDescription(description), null));
                break;
            case PRICE:
                final ReentrantLock lock = new ReentrantLock();
                sendOrderUpdate(channel, Color.ORANGE, cerebrumUser, "orderPriceSetTitle", "orderPriceSetDesc", "orderPriceSetMsg", this,
                        null, null);
                do
                {
                    eventWaiter.waitForEvent(MessageReceivedEvent.class,
                            event ->
                                    event.getMessage().getIdLong() != channel.getLatestMessageIdLong() &&
                                            !event.getAuthor().isBot() &&
                                            event.isFromType(ChannelType.PRIVATE) &&
                                            !event.isFromGuild(),
                            event ->
                            {
                                try
                                {
                                    final String content = event.getMessage().getContentRaw();
                                    int price = Integer.parseInt(content);
                                    sendOrderUpdate(channel, Color.GREEN, cerebrumUser, "opSuccess", "orderMadeProgress", "orderPriceUpdatedMsg", this, order ->
                                            order.setPrice(price), null);
                                    lock.unlock();
                                }
                                catch (NumberFormatException e)
                                {
                                    sendOrderUpdate(channel, Color.RED, cerebrumUser, "opFailed", "opFailedDesc", "orderPriceIncorrect", this,
                                            null, null);
                                }
                            });
                    lock.lock();
                }
                while (!lock.isLocked());
                processNextStep(channel, cerebrumUser);
                break;
            case DEADLINE:
                break;
            case DONE:
                /*sendOrderUpdate(channel, Color.GREEN, cerebrumUser, "opSuccess", "ORDERDONE", "ORDERDONE", this, order ->
                {
                }, null);*/
                break;
        }
    }

    /**
     * Advances the Order to the next step.
     *
     * @param channel      the channel
     * @param cerebrumUser the user
     */
    private void processNextStep(PrivateChannel channel, CerebrumUser cerebrumUser)
    {
        setStep(getNextStep());
        processStep(channel, cerebrumUser);
    }

    /**
     * It just work.
     *
     * @param channel         the channel
     * @param color           the color
     * @param cerebrumUser    the user
     * @param title           the title
     * @param description     the description
     * @param message         the message
     * @param order           the order
     * @param orderConsumer   the order's consumer
     * @param messageConsumer the message's consumer
     * @param msgReplace      what to replace in the message
     */
    private void sendOrderUpdate(PrivateChannel channel, java.awt.Color color, CerebrumUser cerebrumUser, String title, String description, String message, Order order, Consumer<Order> orderConsumer, Consumer<Message> messageConsumer, String... msgReplace)
    {
        final MessageEmbed.Field field = new MessageEmbed.Field(I18NManager.getValue(cerebrumUser.getUserLanguage(), description), I18NManager.getValue(cerebrumUser.getUserLanguage(), message, msgReplace), true);
        final MessageEmbed embed = EmbedMaker.make(color, I18NManager.getValue(cerebrumUser.getUserLanguage(), title), null, field);
        channel.sendMessage(embed).queue(messageConsumer);
        if (orderConsumer != null) { orderConsumer.accept(order); }
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

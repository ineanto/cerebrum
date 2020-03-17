package fr.antoinerochas.cerebrum.order;

import fr.antoinerochas.cerebrum.Cerebrum;
import fr.antoinerochas.cerebrum.i18n.I18N;
import fr.antoinerochas.cerebrum.jda.api.ReactionListener;
import fr.antoinerochas.cerebrum.user.CerebrumUser;
import fr.antoinerochas.cerebrum.utils.Color;
import fr.antoinerochas.cerebrum.embed.ComplexEmbed;
import fr.antoinerochas.cerebrum.utils.EventWaiter;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

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
    private transient OrderStepType step;

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
        this.step = OrderStepType.TYPE;
    }

    /**
     * Process the next Order's step.
     *
     * @param channel      the channel
     * @param cerebrumUser the user
     */
    public void process(PrivateChannel channel, CerebrumUser cerebrumUser)
    {
        final EventWaiter eventWaiter = Cerebrum.getEventWaiter();
        final AtomicBoolean pass = new AtomicBoolean(false);

        final ComplexEmbed successEmbed = new ComplexEmbed(channel, cerebrumUser);
        successEmbed.setColor(Color.GREEN);
        successEmbed.setTitle(I18N.Global.SUCCESS);
        successEmbed.setOrder(this);

        // can be further optimized ?
        switch (step)
        {
            case TYPE:
                // TODO: BUG - Apparently, if you click too fast/interact soon after the bot has been started, the interaction just does not work, strange.
                final ComplexEmbed typeEmbed = new ComplexEmbed(channel, cerebrumUser);
                typeEmbed.setColor(Color.ORANGE);
                typeEmbed.setTitle(I18N.Messages.Order.PROGRESS);
                typeEmbed.setTitleReplace("1", "4", "Type");
                typeEmbed.setDescription(I18N.Messages.Order.TYPE_DESC);
                typeEmbed.setMessage(I18N.Messages.Order.TYPE_MSG);
                typeEmbed.setOrder(this);
                typeEmbed.setMessageConsumer(message ->
                {
                    final ReactionListener<String> handler = new ReactionListener<>(cerebrumUser.getUser().getIdLong(), message.getId());
                    successEmbed.setDescription(I18N.Messages.Order.UPDATE_DESC);
                    successEmbed.setDescriptionReplace("Type");
                    successEmbed.setMessage(I18N.Messages.Order.UPDATE_MSG);
                    // Speaker Emoji -> Discord
                    handler.registerReaction("\uD83D\uDD0A", (ret) ->
                            {
                                successEmbed.setOrderConsumer(order ->
                                {
                                    order.setType(OrderType.DISCORD);
                                    successEmbed.setMessageReplace(type.getName());
                                });
                                successEmbed.send();
                                processNextStep(channel, cerebrumUser);
                                handler.disable();
                            }
                    );
                    // Controller Emoji -> Minecraft🎥🎥
                    handler.registerReaction("\uD83C\uDFAE", (ret) ->
                            {
                                successEmbed.setOrderConsumer(order ->
                                {
                                    order.setType(OrderType.MINECRAFT);
                                    successEmbed.setMessageReplace(type.getName());
                                });
                                successEmbed.send();
                                processNextStep(channel, cerebrumUser);
                                handler.disable();
                            }
                    );
                    // Computer Emoji -> Application
                    handler.registerReaction("\uD83D\uDCBB", (ret) ->
                            {
                                successEmbed.setOrderConsumer(order ->
                                {
                                    order.setType(OrderType.APPLICATION);
                                    successEmbed.setMessageReplace(type.getName());
                                });
                                successEmbed.send();
                                processNextStep(channel, cerebrumUser);
                                handler.disable();
                            }
                    );
                    // Thought Ballon Emoji -> Other
                    handler.registerReaction("\uD83D\uDCAD", (ret) ->
                            {
                                successEmbed.setOrderConsumer(order ->
                                {
                                    order.setType(OrderType.OTHER);
                                    successEmbed.setMessageReplace(type.getName());
                                });
                                successEmbed.send();
                                processNextStep(channel, cerebrumUser);
                                handler.disable();
                            }
                    );
                    Cerebrum.getReactionManager().addReactionListener(Cerebrum.GUILD.getIdLong(), message, handler);
                });
                typeEmbed.send();
                break;
            case DESCRIPTION:
                final ComplexEmbed descriptionEmbed = new ComplexEmbed(channel, cerebrumUser);
                descriptionEmbed.setColor(Color.ORANGE);
                descriptionEmbed.setTitle(I18N.Messages.Order.PROGRESS);
                descriptionEmbed.setTitleReplace("2", "4", "Description");
                descriptionEmbed.setDescription(I18N.Messages.Order.DESCRIPTION_DESC);
                descriptionEmbed.setMessage(I18N.Messages.Order.DESCRIPTION_MSG);
                descriptionEmbed.setOrder(this);
                descriptionEmbed.send();
                eventWaiter.waitForEvent(MessageReceivedEvent.class,
                        event ->
                                event.getMessage().getIdLong() != channel.getLatestMessageIdLong() &&
                                        !event.getAuthor().isBot() &&
                                        event.isFromType(ChannelType.PRIVATE) &&
                                        !event.isFromGuild(),
                        event ->
                        {
                            final String description = event.getMessage().getContentRaw();
                            successEmbed.setDescription(I18N.Messages.Order.UPDATE_DESC);
                            successEmbed.setDescriptionReplace("Description");
                            successEmbed.setMessage(I18N.Messages.Order.UPDATE_MSG);
                            successEmbed.setMessageReplace("Description updated!");
                            successEmbed.send();
                            processNextStep(channel, cerebrumUser);
                        });
                break;
            case PRICE:
                final ReentrantLock lock = new ReentrantLock();
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
                                    System.out.println("price good boi");
                                    /*sendOrderUpdate(channel, Color.GREEN, cerebrumUser, "opSuccess", "orderMadeProgress", "orderPriceUpdatedMsg", this, order ->
                                            order.setPrice(price), null);*/
                                    lock.unlock();
                                }
                                catch (NumberFormatException e)
                                {
                                    System.out.println("price looser bad");
                                    /*sendOrderUpdate(channel, Color.RED, cerebrumUser, "opFailed", "opFailedDesc", "orderPriceIncorrect", this,
                                            null, null);*/
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
        process(channel, cerebrumUser);
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

    public OrderStepType getStep()
    {
        return step;
    }

    public void progressStep()
    {
        setStep(getNextStep());
    }

    public OrderStepType getNextStep()
    {
        OrderStepType step = OrderStepType.values()[getStep().ordinal() + 1];
        return step == null ? OrderStepType.DONE : step;
    }

    public void setStep(OrderStepType step)
    {
        this.step = step;
    }

    public boolean isDone()
    {
        return step == OrderStepType.DONE;
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

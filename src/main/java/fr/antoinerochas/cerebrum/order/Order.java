package fr.antoinerochas.cerebrum.order;

import fr.antoinerochas.cerebrum.Cerebrum;
import fr.antoinerochas.cerebrum.order.framework.Step;
import fr.antoinerochas.cerebrum.order.step.TypeStep;
import fr.antoinerochas.cerebrum.user.CerebrumUser;
import net.dv8tion.jda.api.entities.PrivateChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This file is part of Cerebrum.
 * Represent an Order.
 *
 * @author Aro at/on 30/01/2020
 * @since 1.0
 */
public class Order implements Cloneable {
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
    private transient Step step;

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
    public Order(String customerId, OrderType type, String description, int price, long ordered, long deadline) {
        // Define parameters.
        this.customerId = customerId;
        this.type = type;
        this.description = description;
        this.price = price;
        this.ordered = ordered;
        this.deadline = deadline;
    }

    /**
     * Start the Order.
     */
    public void start(PrivateChannel channel, CerebrumUser cerebrumUser) {
        this.step = new TypeStep(channel, this, cerebrumUser);
        this.step.before(this, cerebrumUser);
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getOrdered() {
        return ordered;
    }

    public void setOrdered(long ordered) {
        this.ordered = ordered;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public Step getStep() {
        return step;
    }

    public void progressStep() {
        setStep(getNextStep());
    }

    public Step getNextStep() {
        final int position = Cerebrum.getOrderManager().getStepManager().getStepPosition(getStep().getClass());
        return step.isLast() ? null : Cerebrum.getOrderManager().getStepManager().getNextStep(position);
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public boolean isDone() {
        return step.isLast();
    }

    @Override
    public String toString() {
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
    public Order clone() {
        try {
            return (Order) super.clone();
        } catch (CloneNotSupportedException e) {
            LOGGER.error("Failed to clone the Order!", e);
            return null;
        }
    }
}

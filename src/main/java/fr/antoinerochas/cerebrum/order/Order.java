package fr.antoinerochas.cerebrum.order;

/**
 * This file is part of Cerebrum.
 * Represent an Order.
 *
 * @author Aro at/on 30/01/2020
 * @since 1.0
 */
public class Order
{
    /**
     * Customer's ID.
     */
    private String customerId;

    /**
     * Order's description.
     */
    private String description;

    /**
     * Order's date.
     */
    private long date;

    /**
     * Order's deadline.
     */
    private long deadline;

    /**
     * Constructor.
     *
     * @param customerId  customer's ID
     * @param description order's description
     * @param date        order's date
     * @param deadline    order's deadline
     */
    public Order(String customerId, String description, long date, long deadline)
    {
        // Define parameters.
        this.customerId = customerId;
        this.description = description;
        this.date = date;
        this.deadline = deadline;
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public long getDate()
    {
        return date;
    }

    public void setDate(long date)
    {
        this.date = date;
    }

    public long getDeadline()
    {
        return deadline;
    }

    public void setDeadline(long deadline)
    {
        this.deadline = deadline;
    }
}

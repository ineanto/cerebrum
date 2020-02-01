package fr.antoinerochas.cerebrum.order;

import java.util.Date;

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
    private Date date;

    /**
     * Order's deadline.
     */
    private Date deadline;

    /**
     * Constructor.
     *
     * @param customerId  customer's ID
     * @param description order's description
     * @param date        order's date
     * @param deadline    order's deadline
     */
    protected Order(String customerId, String description, Date date, Date deadline)
    {
        // Define parameters.
        this.customerId = customerId;
        this.description = description;
        this.date = date;
        this.deadline = deadline;
    }
    
    // TODO: 01/02/2020 JavaDoc ?
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

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public Date getDeadline()
    {
        return deadline;
    }

    public void setDeadline(Date deadline)
    {
        this.deadline = deadline;
    }
}

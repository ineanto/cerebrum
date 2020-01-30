package fr.antoinerochas.cerebrum.order;

/**
 * This file is part of Cerebrum.
 * Enumerates all the different statuses
 * {@link OrderManager} can be in.
 *
 * @author Aro at/on 28/01/2020
 * @since 1.0
 */
public enum OrderStatus
{
    /**
     * The default status. Cerebrum will be able to take orders
     * and send/store them in the official production database.
     */
    AVAILABLE,
    /**
     * If a maintenance is ongoing, or if there's too many orders,
     * we can temporally disable order taking with this status.
     * Cerebrum will inform users that orders will not be processed
     * from now on.
     */
    TEMPORALLY_UNAVAILABLE,
    /**
     * Disable order taking completely.
     */
    OFF,
    /**
     * In Debug status, Cerebrum will not use the official DB.
     */
    DEBUG
}

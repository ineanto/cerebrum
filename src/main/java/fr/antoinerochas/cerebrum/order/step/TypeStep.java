package fr.antoinerochas.cerebrum.order.step;

import fr.antoinerochas.cerebrum.order.Order;
import fr.antoinerochas.cerebrum.order.framework.Step;
import fr.antoinerochas.cerebrum.user.CerebrumUser;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * This file is part of Cerebrum.
 *
 * @author aro on 17/03/2020
 * @since 1.0
 */
public class TypeStep extends Step
{
    /**
     * Constructor.
     *
     * @param channel the channel
     * @param order   the order
     * @param user    the user
     */
    public TypeStep(TextChannel channel, Order order, CerebrumUser user)
    {
        super(channel, order, user);
    }

    @Override
    public String name()
    {
        return "type";
    }

    @Override
    public void before(Message message, Order order, CerebrumUser user)
    {
        if(!check()) panic(order, user);
    }

    @Override
    public void process(Message message, Order order, CerebrumUser user)
    {
    }

    @Override
    public void after(Message message, Order order, CerebrumUser user)
    {

    }

    @Override
    public void panic(Order order, CerebrumUser user)
    {
        // TODO: 17/03/2020 Tell the user something's wrong.
    }
}

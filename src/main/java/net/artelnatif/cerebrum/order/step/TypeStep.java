package net.artelnatif.cerebrum.order.step;

import net.artelnatif.cerebrum.embed.ComplexEmbed;
import net.artelnatif.cerebrum.i18n.I18N;
import net.artelnatif.cerebrum.i18n.Messages;
import net.artelnatif.cerebrum.order.Order;
import net.artelnatif.cerebrum.order.framework.Step;
import net.artelnatif.cerebrum.user.CerebrumUser;
import net.dv8tion.jda.api.entities.MessageChannel;

/**
 * This file is part of Cerebrum.
 *
 * @author aro on 17/03/2020
 * @since 1.0
 */
public class TypeStep extends Step {
    /**
     * Constructor.
     *
     * @param channel the channel
     * @param order   the order
     * @param user    the user
     */
    public TypeStep(MessageChannel channel, Order order, CerebrumUser user) {
        super(channel, order, user);
    }

    @Override
    public String name() {
        return "type";
    }

    @Override
    public void before(Order order, CerebrumUser user) {
        if (!check()) panic(order, user);
        final ComplexEmbed embed = new ComplexEmbed(getChannel(), user);
        embed.setTitle(I18N.get(user, Messages.Order.TYPE_MSG));
        embed.send();
        process(order, user);
    }

    @Override
    public void process(Order order, CerebrumUser user) {
    }

    @Override
    public void after(Order order, CerebrumUser user) {

    }
}

package net.artelnatif.cerebrum.command;

import net.artelnatif.cerebrum.Cerebrum;
import net.artelnatif.cerebrum.command.framework.Command;
import net.artelnatif.cerebrum.command.framework.CommandExecutor;
import net.dv8tion.jda.api.entities.User;

/**
 * This file is part of Cerebrum.
 *
 * @author Aro at/on 28/02/2020
 * @since 1.0
 */
@Command(label = "order")
public class OrderCommand {
    @CommandExecutor
    public void execute(User user) {
        //final CerebrumUser cerebrumUser = Cerebrum.getUserManager().getUser(user);
        Cerebrum.getOrderManager().startOrderProcess(user);
    }
}

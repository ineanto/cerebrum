package fr.antoinerochas.cerebrum.event;

import fr.antoinerochas.cerebrum.Cerebrum;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * This file is part of Cerebrum.
 *
 * @author Aro at/on 30/01/2020
 * @since 1.0
 */
public class MessageListener extends ListenerAdapter {
    /**
     * Listen for an event.
     *
     * @param event the {@link MessageReceivedEvent} event
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // If it's Cerebrum, ignore it.
        if (event.getAuthor().isBot()) {
            return;
        }

        // TODO: 11/10/2020 Use the return value. 
        Cerebrum.getCommandManager().executeCommand(event.getMessage());
    }
}

package fr.antoinerochas.cerebrum.event;

import fr.antoinerochas.cerebrum.Cerebrum;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * This file is part of Cerebrum.
 *
 * @author Aro at/on 30/01/2020
 * @since 1.0
 */
public class MessageListener extends ListenerAdapter
{
    /**
     * Listen for an event.
     *
     * @param event the {@link MessageReceivedEvent} event
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        // If it's a bot message, ignore it.
        if (event.getAuthor() == Cerebrum.getJDA().getSelfUser()) { return; }

        // User's message
        Message message = event.getMessage();

        // If the message received is in DMs and it's "{PREFIX}order"...
        if (event.isFromType(ChannelType.PRIVATE) && message.getContentRaw().equals(Cerebrum.PREFIX + "order"))
        {

        }
    }
}

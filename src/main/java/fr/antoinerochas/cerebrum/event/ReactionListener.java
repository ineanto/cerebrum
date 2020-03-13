package fr.antoinerochas.cerebrum.event;

import fr.antoinerochas.cerebrum.Cerebrum;
import fr.antoinerochas.cerebrum.jda.api.ReactionManager;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

/**
 * This file is part of Cerebrum.
 *
 * @author Aro at/on 04/03/2020
 * @since 1.0
 */
public class ReactionListener extends ListenerAdapter
{
    /**
     * Listen for an event.
     *
     * @param event the {@link MessageReactionAddEvent} event
     */
    @Override
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event)
    {
        // If it's Cerebrum, ignore it.
        if(event.getUser() == null) { return; }
        if (event.getUser().isBot()) { return; }

        final ReactionManager reactionManager = Cerebrum.getReactionManager();
        reactionManager.handle(event.getChannel(), event.getMessageIdLong(), event.getUserIdLong(), event.getReaction());
    }
}
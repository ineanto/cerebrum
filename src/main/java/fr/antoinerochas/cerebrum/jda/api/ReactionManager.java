package fr.antoinerochas.cerebrum.jda.api;

import fr.antoinerochas.cerebrum.Cerebrum;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.internal.utils.PermissionUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This file is part of Cerebrum.
 *
 * @author Aro at/on 03/03/2020
 * @since 1.0
 */
public class ReactionManager
{
    private final ConcurrentHashMap<Long, ConcurrentHashMap<Long, ReactionListener<?>>> reactions;

    public ReactionManager()
    {
        reactions = new ConcurrentHashMap<>();
    }

    public synchronized void addReactionListener(long guildId, Message message, ReactionListener<?> handler)
    {
        addReactionListener(guildId, message, handler, true);
    }

    public synchronized void addReactionListener(long guildId, Message message, ReactionListener<?> handler, boolean queue)
    {
        if (handler == null) { return; }
        if (message.getChannelType().equals(ChannelType.TEXT))
        {
            if (!PermissionUtil.checkPermission(message.getTextChannel(), message.getGuild().getSelfMember(), Permission.MESSAGE_ADD_REACTION))
            {
                return;
            }
        }
        if (!reactions.containsKey(guildId))
        {
            reactions.put(guildId, new ConcurrentHashMap<>());
        }
        if (!reactions.get(guildId).containsKey(message.getIdLong()))
        {
            for (String emote : handler.getEmotes())
            {
                RestAction<Void> action = message.addReaction(emote);
                if (queue) action.queue();
                else action.complete();
            }
        }
        reactions.get(guildId).put(message.getIdLong(), handler);
    }

    public synchronized void removeReactionListener(long guildId, long messageId)
    {
        if (!reactions.containsKey(guildId)) return;
        reactions.get(guildId).remove(messageId);
    }

    /**
     * Handles the reaction
     *
     * @param channel   TextChannel of the message
     * @param messageId id of the message
     * @param userId    id of the user reacting
     * @param reaction  the reaction
     */
    public void handle(MessageChannel channel, long messageId, long userId, MessageReaction reaction)
    {
        ReactionListener<?> listener = reactions.get(Cerebrum.GUILD.getIdLong()).get(messageId);
        if (listener == null) { return; }
        if (!listener.isActive() || listener.getExpiresInTimestamp() < System.currentTimeMillis())
        {
            reactions.remove(Cerebrum.GUILD.getIdLong()).remove(messageId);
        }
        else if ((listener.hasReaction(reaction.getReactionEmote().getName())) && listener.getUserId() == userId)
        {
            reactions.get(Cerebrum.GUILD.getIdLong()).get(messageId).updateLastAction();
            Message message = channel.retrieveMessageById(messageId).complete();
            listener.react(reaction.getReactionEmote().getName(), message);
        }
    }

    /**
     * Do we have an event for a message?
     *
     * @param guildId   discord guild-id of the message
     * @param messageId id of the message
     * @return do we have an handler?
     */
    public boolean canHandle(long guildId, long messageId)
    {
        return reactions.containsKey(guildId) && reactions.get(guildId).containsKey(messageId);
    }

    public synchronized void removeGuild(long guildId)
    {
        reactions.remove(guildId);
    }

    /**
     * Delete expired handlers
     */
    public synchronized void cleanCache()
    {
        long now = System.currentTimeMillis();
        for (Map.Entry<Long, ConcurrentHashMap<Long, ReactionListener<?>>> mapEntry : reactions.entrySet())
        {
            mapEntry.getValue().values().removeIf(listener -> !listener.isActive() || listener.getExpiresInTimestamp() < now);
            if (mapEntry.getValue().values().isEmpty())
            {
                reactions.remove(mapEntry.getKey());
            }
        }
    }
}
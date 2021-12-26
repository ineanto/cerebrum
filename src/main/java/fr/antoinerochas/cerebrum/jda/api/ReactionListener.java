package fr.antoinerochas.cerebrum.jda.api;

import net.dv8tion.jda.api.entities.Message;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * This file is part of Cerebrum.
 *
 * @author Aro at/on 03/03/2020
 * @since 1.0
 */
public class ReactionListener<T> {
    private final Map<String, Consumer<Message>> reactions;
    private final long userId;
    private volatile T data;
    private long expiresIn, lastAction;
    private boolean active;

    public ReactionListener(long userId, T data) {
        this.data = data;
        this.userId = userId;
        reactions = new LinkedHashMap<>();
        active = true;
        lastAction = System.currentTimeMillis();
        expiresIn = TimeUnit.MINUTES.toMillis(1);
    }

    public boolean isActive() {
        return active;
    }

    public void disable() {
        this.active = false;
    }

    /**
     * The time after which this listener expires which is now + specified time
     * Defaults to now+5min
     *
     * @param timeUnit time units
     * @param time     amount of time units
     */
    public void setExpiresIn(TimeUnit timeUnit, long time) {
        expiresIn = timeUnit.toMillis(time);
    }

    /**
     * Check if this listener has specified emote
     *
     * @param emote the emote to check for
     * @return does this listener do anything with this emote?
     */
    public boolean hasReaction(String emote) {
        return reactions.containsKey(emote);
    }

    /**
     * React to the reaction :')
     *
     * @param emote   the emote used
     * @param message the message bound to the reaction
     */
    public void react(String emote, Message message) {
        if (hasReaction(emote)) reactions.get(emote).accept(message);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * Register a consumer for a specified emote
     * Multiple emote's will result in overriding the old one
     *
     * @param emote    the emote to respond to
     * @param consumer the behaviour when emote is used
     */
    public void registerReaction(String emote, Consumer<Message> consumer) {
        reactions.put(emote, consumer);
    }

    /**
     * @return list of all emotes used in this reaction listener
     */
    public Set<String> getEmotes() {
        return reactions.keySet();
    }

    /**
     * updates the timestamp when the reaction was last accessed
     */
    public void updateLastAction() {
        lastAction = System.currentTimeMillis();
    }

    /**
     * When does this reaction listener expire?
     *
     * @return timestamp in millis
     */
    public Long getExpiresInTimestamp() {
        return lastAction + expiresIn;
    }

    public long getUserId() {
        return userId;
    }
}

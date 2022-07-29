package net.artelnatif.cerebrum.order;

/**
 * This file is part of Cerebrum.
 * Represents {@link Order}'s type.
 *
 * @author Aro at/on 08/03/2020
 * @since 1.0
 */
public enum OrderType {
    DISCORD("Discord"),
    MINECRAFT("Minecraft"),
    APPLICATION("Application"),
    OTHER("Other/Autre");

    /**
     * Type's "name"/description.
     */
    private final String name;

    /**
     * Constructor.
     *
     * @param name the type's name
     */
    OrderType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

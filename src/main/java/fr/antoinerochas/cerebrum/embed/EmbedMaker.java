package fr.antoinerochas.cerebrum.embed;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.Color;

/**
 * This file is part of Cerebrum.
 * Utils to create {@link MessageEmbed}.
 *
 * @author Aro at/on 29/02/2020
 * @since 1.0
 */
public class EmbedMaker
{
    public static final MessageEmbed.Field EMPTY_FIELD = new MessageEmbed.Field("\u200e", "\u200e", false);

    public static MessageEmbed make(Color color, String title, String footer, MessageEmbed.Field... fields)
    {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(color);
        builder.setTitle(title);
        builder.setFooter(footer);

        for (MessageEmbed.Field field : fields)
        {
            builder.addField(field);
        }

        return builder.build();
    }
}

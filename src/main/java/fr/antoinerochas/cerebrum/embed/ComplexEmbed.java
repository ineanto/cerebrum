package fr.antoinerochas.cerebrum.embed;

import fr.antoinerochas.cerebrum.i18n.I18NManager;
import fr.antoinerochas.cerebrum.order.Order;
import fr.antoinerochas.cerebrum.user.CerebrumUser;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.awt.Color;
import java.util.function.Consumer;

/**
 * This file is part of Cerebrum.
 *
 * @author Aro at/on 10/03/2020
 * @since 1.0
 */
public class ComplexEmbed
{
    private final MessageChannel channel;
    private final CerebrumUser user;
    private Color color;
    private String title, description, message;
    private Order order;
    private Consumer<Order> orderConsumer = null;
    private Consumer<Message> messageConsumer = null;
    private String[] titleReplace, descriptionReplace, messageReplace;

    public ComplexEmbed(MessageChannel channel, CerebrumUser user)
    {
        this.channel = channel;
        this.user = user;
    }

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Order getOrder()
    {
        return order;
    }

    public void setOrder(Order order)
    {
        this.order = order;
    }

    public Consumer<Order> getOrderConsumer()
    {
        return orderConsumer;
    }

    public void setOrderConsumer(Consumer<Order> orderConsumer)
    {
        this.orderConsumer = orderConsumer;
    }

    public Consumer<Message> getMessageConsumer()
    {
        return messageConsumer;
    }

    public void setMessageConsumer(Consumer<Message> messageConsumer)
    {
        this.messageConsumer = messageConsumer;
    }

    public String[] getTitleReplace()
    {
        return titleReplace;
    }

    public void setTitleReplace(String... titleReplace)
    {
        this.titleReplace = titleReplace;
    }

    public String[] getDescriptionReplace()
    {
        return descriptionReplace;
    }

    public void setDescriptionReplace(String... descriptionReplace)
    {
        this.descriptionReplace = descriptionReplace;
    }

    public String[] getMessageReplace()
    {
        return messageReplace;
    }

    public void setMessageReplace(String... messageReplace)
    {
        this.messageReplace = messageReplace;
    }

    public void send()
    {
        final MessageEmbed.Field field = new MessageEmbed.Field(I18NManager.getValue(user.getUserLanguage(), description, descriptionReplace), I18NManager.getValue(user.getUserLanguage(), message, messageReplace), true);
        final MessageEmbed embed = EmbedMaker.make(color, I18NManager.getValue(user.getUserLanguage(), title, titleReplace), null, field);
        final MessageAction messageAction = channel.sendMessage(embed);

        if (orderConsumer != null)
        {
            orderConsumer.accept(order);
        }

        if(messageConsumer != null)
        {
            messageAction.queue(messageConsumer);
        }
        else
        {
            messageAction.queue();
        }
    }
}

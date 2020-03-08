package fr.antoinerochas.cerebrum.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import fr.antoinerochas.cerebrum.order.Order;
import fr.antoinerochas.cerebrum.order.OrderManager;
import fr.antoinerochas.cerebrum.order.OrderType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * This file is part of Cerebrum.
 * Serializes/Deserializes {@link Order} class.
 *
 * @author Aro at/on 26/02/2020
 * @since 1.0
 */
public class OrderTypeAdapter extends TypeAdapter<Order>
{
    /**
     * Log4J's {@link Logger} instance.
     */
    public static final Logger LOGGER = LogManager.getLogger(OrderTypeAdapter.class);

    @Override
    public void write(JsonWriter out, Order value) throws IOException
    {
        out.beginArray();
        out.name("customer").value(value.getCustomerId());
        out.name("type").value(value.getType().ordinal());
        out.name("description").value(value.getDescription());
        out.name("price").value(value.getPrice());
        out.name("ordered").value(value.getOrdered());
        out.name("deadline").value(value.getDeadline());
        out.endArray();
    }

    @Override
    public Order read(JsonReader in) throws IOException
    {
        Order defaultOrder = OrderManager.DEFAULT_ORDER;
        String customer = defaultOrder.getCustomerId(), description = defaultOrder.getDescription();
        OrderType type = defaultOrder.getType();
        int price = defaultOrder.getPrice();
        long ordered = defaultOrder.getOrdered(), deadline = defaultOrder.getDeadline();

        in.beginArray();
        while (in.hasNext())
        {
            customer = in.nextString();
            type = OrderType.values()[in.nextInt()];
            description = in.nextString();
            price = in.nextInt();
            ordered = in.nextLong();
            deadline = in.nextLong();
        }
        in.endArray();

        return new Order(customer, type, description, price, ordered, deadline);
    }
}

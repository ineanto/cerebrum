package fr.antoinerochas.cerebrum.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import fr.antoinerochas.cerebrum.i18n.I18NManager;
import fr.antoinerochas.cerebrum.order.Order;
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
        out.name("description").value(value.getDescription());
        out.name("ordered").value(value.getDate());
        out.name("deadline").value(value.getDeadline());
        out.endArray();
    }

    @Override
    public Order read(JsonReader in) throws IOException
    {
        String customer = I18NManager.getValue("orderNullValue"), description = I18NManager.getValue("orderNullValue");
        long ordered = -1, deadline = -1;

        in.beginArray();
        while (in.hasNext())
        {
            customer = in.nextString();
            description = in.nextString();
            ordered = in.nextLong();
            deadline = in.nextLong();
        }
        in.endArray();

        return new Order(customer, description, ordered, deadline);
    }
}

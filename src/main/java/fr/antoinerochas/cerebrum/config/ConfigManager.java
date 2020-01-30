package fr.antoinerochas.cerebrum.config;

import com.google.gson.reflect.TypeToken;
import fr.antoinerochas.cerebrum.json.GsonManager;
import net.dv8tion.jda.api.entities.Guild;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This file is part of Cerebrum.
 * It manages bot's configuration.
 *
 * @author Aro at/on 28/01/2020
 * @since 1.0
 */
public class ConfigManager
{
    /**
     * Log4J's {@link Logger} instance.
     */
    public static final Logger LOGGER = LogManager.getLogger(ConfigManager.class);

    /**
     * The "physical" configuration file.
     */
    private static File configuration;

    /**
     * Private constructor to prevent unwanted initialization.
     */
    private ConfigManager() {}

    /**
     * Loads the configuration file and <p>
     * return an instance of {@link ConfigManager}.
     *
     * @return an instance of {@link ConfigManager}
     */
    public static ConfigManager loadConfiguration() throws IOException
    {
        File file = new File("./config.json");
        InputStream resource = ClassLoader.getSystemResourceAsStream("config.json");

        if (!file.exists())
        {
            Files.copy(resource, file.toPath());
            LOGGER.info("Please define the channels and then restart the bot!");
            System.exit(0);
        }

        configuration = file;

        // Return a new instance of ConfigManager.
        return new ConfigManager();
    }

    /**
     * Get the {@link Guild} ID.
     *
     * @return the {@link Guild} ID present in configuration
     */
    public String getGuildId()
    {
        return (String) getValue("guildId");
    }

    /**
     * Get the order channel ID.
     *
     * @return the order channel ID present in configuration
     */
    public String getOrderChannelId()
    {
        return (String) getValue("orderChannel");
    }

    /**
     * Get the log channel ID.
     *
     * @return the log channel ID present in configuration
     */
    public String getLogChannelId()
    {
        return (String) getValue("logChannel");
    }

    /**
     * Get bot's operators.
     *
     * @return all the bot operators as a {@link ArrayList}
     */
    public ArrayList<?> getOperatorsIds()
    {
        return (ArrayList<?>) getValue("operators");
    }

    /**
     * Get a value associated to <p>
     * the {@code key} from configuration file.
     *
     * @param key the value's key
     * @return the {@link Object} value linked to the key
     */
    public Object getValue(String key)
    {
        LOGGER.debug("Reading " + key + " from the configuration...");
        // Instantiate an HashMap type.
        final Type hashMapType = new TypeToken<HashMap<String, Object>>() {}.getType();

        // Try to create a reader from the language file.
        try (BufferedReader reader = new BufferedReader(new FileReader(configuration)))
        {
            LOGGER.debug("Typing object...");
            // Read the JSON file and convert it's contents into an HashMap.
            final HashMap<String, Object> map = GsonManager.loadFile(reader, hashMapType);
            LOGGER.debug("Typed successfully, returning key.");

            // Get the value from the Map according to the key.
            final Object value = map.get(key);

            // If the key is null, return the key and give an error in console.
            if (value == null)
            {
                LOGGER.error(key + " has not been found in configuration.");
                return key;
            }

            // Else, return the value.
            return value;
        }
        catch (IOException ex)
        {
            // If we fail I/O notify the user and stop the application
            LOGGER.error("Failed to read configuration!", ex);
            System.exit(-1);
            return null;
        }
    }
}

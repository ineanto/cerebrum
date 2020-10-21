package fr.antoinerochas.cerebrum.i18n;

import com.google.gson.reflect.TypeToken;
import fr.antoinerochas.cerebrum.Cerebrum;
import fr.antoinerochas.cerebrum.json.GsonManager;
import fr.antoinerochas.cerebrum.user.CerebrumUser;
import net.dv8tion.jda.api.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * This file is part of Cerebrum.
 * Manage all the I18N of Cerebrum.
 *
 * @author Aro at/on 28/01/2020
 * @since 1.0
 */
public class I18N
{
    /**
     * Bot's default language.
     */
    public static final Language DEFAULT_LANGUAGE = Language.ENGLISH;

    /**
     * Log4J's {@link Logger} instance.
     */
    public static final Logger LOGGER = LogManager.getLogger(I18N.class);

    /**
     * Get a value associated to <p>
     * the {@code key} from language file.
     *
     * @param key the value's key
     * @return the {@link String} value linked to the key
     */
    public static String get(Language language, String key, String... replace)
    {
        if (language == null) language = I18N.DEFAULT_LANGUAGE;
        if (key == null) { return null; }

        LOGGER.debug("I18N Fetch (l=" + language.getCode() + ",k=" + key + ")...");

        // Instantiate an HashMap type.
        final Type hashMapType = new TypeToken<HashMap<String, String>>() {}.getType();

        // Try to create a reader from the language file.
        try (BufferedReader reader = new BufferedReader(new FileReader(language.getFile())))
        {
            // Read the JSON file and convert it's contents into an HashMap.
            final HashMap<String, String> map = GsonManager.loadFile(reader, hashMapType);

            // Get the value from the Map according to the key.
            String value = map.get(key);

            // If the key is null, return the key and give an error in console.
            if (value == null)
            {
                LOGGER.error("\"" + key + "\" can't be found in " + language.getCode() + " (" + language.getCode() + "), skipping");
                return "Not Found! (" + key + " in " + language.getCode() + ")";
            }

            // Log if succeeded.
            LOGGER.debug("Success.");

            if (replace != null && replace.length != 0)
            {
                for (int i = 0; i < replace.length; i++)
                {
                    value = value.replace("{" + i + "}", replace[i]);
                }
            }

            return value;
        }
        catch (IOException ex)
        {
            // If we fail I/O notify the user and stop the application
            LOGGER.error("Failed to read: " + language.getFile().getName() + "(" + language.getCode() + ")!", ex);
            return language.getCode() + "-" + key;
        }
    }

    /**
     * Get a value associated with the
     * <p>
     * {@code key} from the default English language file.
     *
     * @param key the value's key
     * @return the {@link String} value linked to the key
     */
    public static String get(String key)
    {
        // Same, but default the language to English.
        return get(Language.ENGLISH, key);
    }

    /**
     * Get a value associated with the
     * <p>
     * {@code key} from the default English language file.
     *
     * @param key the value's key
     * @return the {@link String} value linked to the key
     */
    public static String get(CerebrumUser user, String key)
    {
        // Same, but default the language to English.
        return get(getUserLanguage(user.getUser()), key);
    }

    /**
     * Get the user's language.
     *
     * @param user the user
     * @return user's language
     */
    public static Language getUserLanguage(User user)
    {
        CerebrumUser cerebrumUser = Cerebrum.getUserManager().getUser(user);

        // By default, return the English language.
        if (cerebrumUser == null) return Language.ENGLISH;
        return Language.values()[cerebrumUser.getLanguage()];
    }
}

package fr.antoinerochas.cerebrum.user;

import com.google.gson.reflect.TypeToken;
import fr.antoinerochas.cerebrum.i18n.Language;
import fr.antoinerochas.cerebrum.json.GsonManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This file is part of Cerebrum.
 * Manages User information.
 *
 * @author Aro at/on 30/01/2020
 * @since 1.0
 */
public class UserManager
{
    /**
     * Log4J's {@link Logger} instance.
     */
    public static final Logger LOGGER = LogManager.getLogger(UserManager.class);

    /**
     * Represents the current {@link JDA}'s instance <p>
     * {@code Cerebrum} is currently using.
     */
    private JDA jda;

    /**
     * Represents all loaded users.
     */
    private final HashMap<String, CerebrumUser> users = new HashMap<>();

    /**
     * Represents user data directory.
     */
    private final File userDirectory = new File("./user/");

    /**
     * Constructor.
     *
     * @param jda the current {@link JDA} instance
     */
    public UserManager(JDA jda)
    {
        LOGGER.debug("Loading UserManager...");
        // Define the JDA instance.
        this.jda = jda;
    }

    /**
     * Gets a {@link CerebrumUser} from a {@link User}.
     *
     * @return {@link CerebrumUser}'s instance containing all user data
     */
    public CerebrumUser getUser(User user)
    {
        return users.get(user.getId());
    }

    /**
     * Loads {@link User}'s data.
     *
     * @param user the user that we have to load data from
     */
    private void loadUser(User user)
    {
        // Get user's file.
        final File userFile = getUserFile(user);

        if (checkUserDataFolder())
        {
            boolean validUserData = false;
            if (!userFile.exists())
            {
                validUserData = createUserData(user);
            }

            if (validUserData)
            {
                // Load the file and return it as CerebrumUser object.
                final Type cerebrumUserType = new TypeToken<CerebrumUser>() {}.getType();

                try
                {
                    CerebrumUser cerebrumUser = GsonManager.loadFile(new BufferedReader(new FileReader(userFile)), cerebrumUserType);
                    users.putIfAbsent(cerebrumUser.getId(), cerebrumUser);
                }
                catch (FileNotFoundException e)
                {
                    LOGGER.error("Failed to load user " + user.getId() + "!");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Creates {@link User}'s data.
     *
     * @param user the user that we have to create data
     */
    public boolean createUserData(User user)
    {
        // Get user's file.
        final File userFile = getUserFile(user);

        if (checkUserDataFolder())
        {
            // Some logs.
            LOGGER.info("Creating data for user " + user.getId() + "...");

            // Instantiate CerebrumUser and turn it to JSON.
            final CerebrumUser cerebrumUser = new CerebrumUser(Language.ENGLISH.ordinal(), new ArrayList<>(), -1, user.getId());
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile)))
            {
                // Create the file if it does not exists.
                if (!userFile.exists())
                {
                    userFile.createNewFile();
                }

                writer.write(GsonManager.GSON.toJson(cerebrumUser));
                writer.close();
                return true;
            }
            catch (IOException e)
            {
                LOGGER.error("Failed to create data for user " + user.getId() + "!");
                e.printStackTrace();
                return false;
            }
        }

        return false;
    }

    /**
     * Check if the User Data folder is present,
     * and creates it if not.
     *
     * @return {@link Boolean#TRUE} if checked, {@link Boolean#FALSE} otherwise
     */
    private boolean checkUserDataFolder()
    {
        // If the "user" directory doesn't exist, create it.
        if (!userDirectory.exists())
        {
            return userDirectory.mkdirs();
        }

        return true;
    }

    /**
     * Get user's data {@link File}.
     *
     * @param user the user we have to get the file from
     * @return the user's data {@link File}
     */
    private File getUserFile(User user)
    {
        return new File(userDirectory, user.getId() + ".json");
    }
}

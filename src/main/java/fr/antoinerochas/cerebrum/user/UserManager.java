package fr.antoinerochas.cerebrum.user;

import fr.antoinerochas.cerebrum.i18n.Language;
import fr.antoinerochas.cerebrum.json.GsonManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
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
     * Represents the current {@link JDA}'s instance
     * <p>
     * {@code Cerebrum} is currently using.
     */
    private final JDA jda;

    /**
     * Represents all loaded users.
     */
    private final HashMap<String, CerebrumUser> users = new HashMap<>();

    /**
     * Represents {@code Cerebrum}'s operators.
     */
    private final ArrayList<String> operators = new ArrayList<>();

    /**
     * Represents user data directory.
     */
    private final File userDirectory = new File("./users/");

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
        if (users.get(user.getId()) == null)
        {
            LOGGER.debug("User " + user.getId() + " is not into cache");
            loadUser(user);
        }

        return users.get(user.getId());
    }

    /**
     * Save all {@link CerebrumUser}s.
     */
    public void saveAll()
    {
        LOGGER.info("Saving users...");
        // How much users we have saved.
        int saved = 0;
        for (CerebrumUser value : users.values())
        {
            // Save the user.
            saved++;
            saveUser(value.getUser());
        }
        // Clear the cache.
        users.clear();
        LOGGER.info("Saved " + saved + " users.");
    }

    /**
     * Creates {@link User}'s data.
     *
     * @param user the user that we have to create data
     */
    private boolean createUserData(User user)
    {
        if (checkUserDataFolder())
        {
            // Get user's file.
            final File userFile = getUserFile(user);

            // If the file already exists, return true.
            if (userFile.exists()) { return true; }

            // Some logs.
            LOGGER.debug("Creating data for user " + user.getId() + "...");

            // Instantiate CerebrumUser and turn it to JSON.
            final CerebrumUser cerebrumUser = new CerebrumUser(user.getId(), Language.ENGLISH.ordinal(), new ArrayList<>(), -1);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile)))
            {
                // Create the file if it does not exists.
                if (!userFile.exists()) { userFile.createNewFile(); }

                writer.write(GsonManager.GSON.toJson(cerebrumUser));
                writer.close();
                return true;
            }
            catch (IOException e)
            {
                LOGGER.error("Failed to create data for user " + user.getId() + "!", e);
                System.exit(-1);
                return false;
            }
        }

        return false;
    }

    /**
     * Loads {@link User}'s data.
     *
     * @param user the user that we have to load data from
     */
    private void loadUser(User user)
    {
        if (checkUserDataFolder())
        {
            // Get user's file.
            final File userFile = getUserFile(user);
            final boolean validUserData = createUserData(user);
            if (validUserData)
            {
                try
                {
                    CerebrumUser cerebrumUser = GsonManager.loadFile(new BufferedReader(new FileReader(userFile)), CerebrumUser.class);
                    users.put(cerebrumUser.getId(), cerebrumUser);
                    LOGGER.info("Successfully loaded user " + user.getId());
                }
                catch (FileNotFoundException e)
                {
                    LOGGER.error("Failed to load user " + user.getId() + "!", e);
                    System.exit(-1);
                }
            }
        }
    }

    /**
     * Saves {@link User}'s data.
     *
     * @param user the user that we have to save data
     */
    private void saveUser(User user)
    {
        if (checkUserDataFolder())
        {
            // Get user's file.
            final File userFile = getUserFile(user);
            final boolean validUserData = getUser(user) != null;
            if (validUserData)
            {
                // Instantiate CerebrumUser and turn it to JSON.
                final CerebrumUser cerebrumUser = getUser(user);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile)))
                {
                    writer.write(GsonManager.GSON.toJson(cerebrumUser));
                }
                catch (IOException e)
                {
                    LOGGER.error("Failed to save data for user " + user.getId() + "!", e);
                    System.exit(-1);
                }
            }
        }
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

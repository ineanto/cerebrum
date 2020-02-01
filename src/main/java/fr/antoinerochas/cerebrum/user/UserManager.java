package fr.antoinerochas.cerebrum.user;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
     * Load's a {@link User}'s data.
     *
     * @param user the user that we have to load data from
     * @return {@link UserData}'s instance containing all user data
     */
    public UserData loadUserData(User user)
    {


        // If we don't succeed to load user's data, return nothing.
        return null;
    }
}

package fr.antoinerochas.cerebrum.jda;

import fr.antoinerochas.cerebrum.event.MessageListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

/**
 * This file is part of Cerebrum.
 * Tries to get an {@link JDA} instance,
 * using the env. var "DISCORD_SECRET".
 *
 * @author Aro at/on 27/01/2020
 * @since 1.0
 */
public class JDAManager
{
    /**
     * Log4J's {@link Logger} instance.
     */
    public static final Logger LOGGER = LogManager.getLogger(JDAManager.class);

    /**
     * <b>Must remains private at all costs.</b>
     * <p>
     * This is the environment variable (env. var/envar) name
     * representing the secret Discord token used
     * to log into the Discord Bot account and allow
     * {@link JDA} to work.
     */
    private final String envar = "DISCORD_SECRET";

    /**
     * Log-in the Discord bot account using the secret {@link #envar}.
     *
     * @return a connected instance of {@link JDA}
     * @throws LoginException       if the token is not valid or Discord is not available
     * @throws InterruptedException if {@link JDA}'s build is interrupted
     */
    public JDA login() throws LoginException, InterruptedException
    {
        // Get the envar value.
        LOGGER.debug("Checking for the envar...");
        String token = System.getenv(envar);
        LOGGER.debug(token == null ? "envar found!" : "envar not found (ran in IntelliJ ?)...");

        // If the envar is null or not present, exit.
        if (token == null)
        {
            LOGGER.error("envar is not present!");
            System.exit(-1);
        }

        // Pass it through JDABuilder.
        JDABuilder builder = new JDABuilder(token);
        LOGGER.info("Building Bot...");

        // Modify some attributes.
        builder.addEventListeners(new MessageListener());
        builder.setDisabledCacheFlags(EnumSet.of(CacheFlag.VOICE_STATE)); // Don't cache user's voice state.
        builder.setActivity(Activity.watching("you !")); // Modify bot's activity.
        builder.setStatus(OnlineStatus.ONLINE); // Set his status to Online.

        // Wait JDA to finish building and return the newly created instance.
        LOGGER.info("Bot built!");
        return builder.build().awaitStatus(JDA.Status.CONNECTED);
    }
}

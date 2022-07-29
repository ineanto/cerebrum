package net.artelnatif.cerebrum.jda;

import net.artelnatif.cerebrum.config.ConfigManager;
import net.artelnatif.cerebrum.event.MessageListener;
import net.artelnatif.cerebrum.event.ReactionListener;
import net.artelnatif.cerebrum.utils.EventWaiter;
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
public class JDAManager {
    /**
     * Log4J's {@link Logger} instance.
     */
    public static final Logger LOGGER = LogManager.getLogger(JDAManager.class);

    /**
     * <b>Must remains private at all costs.</b>
     * <p>
     * This is the environment variable (env. var/envar)
     * representing the secret Discord token used
     * to log into the Discord Bot account and allow
     * {@link JDA} to work.
     */
    private final String envar = "DISCORD_SECRET";

    /**
     * The {@link EventWaiter} instance.
     */
    private EventWaiter eventWaiter;

    /**
     * The {@link ConfigManager} instance.
     */
    private ConfigManager configManager;

    /**
     * Constructor.
     *
     * @param eventWaiter   the {@link EventWaiter} instance
     * @param configManager the {@link ConfigManager} instance
     */
    public JDAManager(EventWaiter eventWaiter, ConfigManager configManager) {
        this.eventWaiter = eventWaiter;
        this.configManager = configManager;
    }

    /**
     * Log-in the Discord bot account using the secret {@link #envar}.
     *
     * @return a connected instance of {@link JDA}
     * @throws LoginException       if the token is not valid or Discord is not available
     * @throws InterruptedException if {@link JDA}'s build is interrupted
     */
    public JDA login() throws LoginException, InterruptedException {
        // Get the envar value.
        LOGGER.debug("Checking for the envar...");
        String token = System.getenv(envar);
        LOGGER.debug(token == null ? "envar not found, skipping" : "envar found");

        LOGGER.info("Reading Token from Configuration file...");
        token = configManager.getToken();

        // If the envar is null, exit.
        if (token == null || token.equals("token")) {
            LOGGER.error("Token can't be found.");
            System.exit(-1);
        }

        // Pass it through JDABuilder.
        JDABuilder builder = JDABuilder.createDefault(token);

        LOGGER.info("Building Bot...");

        // Modify some attributes.
        builder.addEventListeners(new MessageListener(), eventWaiter, new ReactionListener());
        builder.disableCache(EnumSet.of(CacheFlag.VOICE_STATE)); // Don't cache user's voice state.
        builder.setActivity(Activity.watching("you !")); // Modify bot's activity.
        builder.setStatus(OnlineStatus.ONLINE); // Set his status to Online.

        // Wait JDA to finish building and return the newly created instance.
        LOGGER.info("JDA is ready.");
        return builder.build().awaitStatus(JDA.Status.CONNECTED);
    }
}

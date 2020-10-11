package fr.antoinerochas.cerebrum;

import fr.antoinerochas.cerebrum.command.LanguageCommand;
import fr.antoinerochas.cerebrum.command.framework.CommandManager;
import fr.antoinerochas.cerebrum.command.OrderCommand;
import fr.antoinerochas.cerebrum.config.ConfigManager;
import fr.antoinerochas.cerebrum.console.CLIManager;
import fr.antoinerochas.cerebrum.console.CLIReload;
import fr.antoinerochas.cerebrum.console.CLIStop;
import fr.antoinerochas.cerebrum.embed.ComplexEmbed;
import fr.antoinerochas.cerebrum.i18n.I18N;
import fr.antoinerochas.cerebrum.jda.JDAManager;
import fr.antoinerochas.cerebrum.jda.api.ReactionManager;
import fr.antoinerochas.cerebrum.order.OrderManager;
import fr.antoinerochas.cerebrum.user.UserManager;
import fr.antoinerochas.cerebrum.utils.Color;
import fr.antoinerochas.cerebrum.utils.EventWaiter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.Scanner;

/**
 * This file is part of Cerebrum.
 * <p>
 * It contains Cerebrum's entry point and
 * initializes the first so-called "modules"
 * (e.g.: User Management, Database Management).
 *
 * @author Aro at/on 27/01/2020
 * @since 1.0
 */
public class Cerebrum
{
    /**
     * Log4J's {@link Logger} instance.
     * <p>
     * Used mainly at boot, as separate modules all have a
     * <p>
     * dedicated {@link Logger} instance to facilitate debugging.
     */
    public static final Logger LOGGER = LogManager.getLogger(Cerebrum.class);

    /**
     * Application's name.
     */
    public static final String APP = "Cerebrum";

    /**
     * {@code Cerebrum}'s version.
     */
    public static final String VERSION = "1.0-SNAPSHOT";

    /**
     * {@code Cerebrum}'s prefix.
     */
    public static final String PREFIX = "?";

    /**
     * {@code Cerebrum}'s owner's ID.
     */
    public static final String OWNER = "281511510176563201";

    /**
     * Determines if {@code Cerebrum} is in debug mode.
     * <p>
     * Enabling debug will provide additional information
     * <p>
     * about {@code Cerebrum}'s work, requests, activities and much more.
     */
    public static boolean DEBUG = false;

    /**
     * Determines if {@code Cerebrum} is currently running.
     */
    public static boolean RUNNING = false;

    /**
     * The {@link Guild} the bot's linked to.
     */
    public static Guild GUILD;

    /**
     * The debug flag that will trigger {@code Cerebrum}
     * <p>
     * to enable debugging and set {@link #DEBUG} to {@link Boolean#TRUE}.
     */
    private static final String debugFlag = "-DEBUG";

    /**
     * Represents the current {@link JDA}'s instance
     * <p>
     * {@code Cerebrum} is currently using.
     */
    private static JDA jda;

    /**
     * The {@link OrderManager} instance.
     */
    private static OrderManager orderManager;

    /**
     * The {@link UserManager} instance.
     */
    private static UserManager userManager;

    /**
     * The {@link ConfigManager} instance.
     */
    private static ConfigManager configManager;

    /**
     * The {@link CommandManager} instance.
     */
    private static CommandManager commandManager;

    /**
     * The {@link CLIManager} instance.
     */
    private static CLIManager cliManager;

    /**
     * The {@link ReactionManager} instance.
     */
    private static ReactionManager reactionManager;

    /**
     * The {@link EventWaiter} instance.
     */
    private static EventWaiter eventWaiter;

    /**
     * Java's application entry point.
     *
     * @param args arguments
     */
    public static void main(String[] args) throws IOException
    {
        // Check for the debugging flag.
        if (args.length > 0 && args[0].equals(debugFlag))
        {
            DEBUG = true;
            Configurator.setRootLevel(Level.DEBUG);
            LOGGER.debug("Debug Mode has been enabled. " + APP + " will not be able to take");
            LOGGER.debug("any orders and will use a fake database.");
            LOGGER.debug("(You can disable Debug Mode by removing the \"-DEBUG\" flag.)");
        }

        // Launch Cerebrum.
        LOGGER.info("Starting " + APP + " " + VERSION + "...");
        LOGGER.info("Copyright (c) 2020 Antoine \"Aro\" ROCHAS.");

        // Instantiate ConfigManager.
        configManager = new ConfigManager();
        configManager.loadConfiguration();

        // Instantiate JDAManager.
        final JDAManager jdaManager = new JDAManager(eventWaiter = new EventWaiter(), configManager);

        // Try to connect to JDA.
        try
        {
            // Connection succeeded!
            jda = jdaManager.login();
            configManager.setJda(jda);
            LOGGER.info("Logged into JDA.");

            // Checking channels validity...
            LOGGER.info("Validating channels...");
            final String guildId = configManager.getGuildId();
            GUILD = jda.getGuildById(guildId);
            if (GUILD == null)
            {
                LOGGER.error("Can't find Guild/Server with ID: ${GUILDID}!".replace("${GUILDID}", guildId));
                System.exit(-1);
                return;
            }
            LOGGER.info("Guild found (name=\"${GUILD}\", id=${GUILDID}).".replace("${GUILD}", GUILD.getName()).replace("${GUILDID}", guildId));

            // Getting channels from the configuration.
            if (configManager.getLogChannelId().isEmpty())
            {
                LOGGER.warn("Log channel hasn't been defined. Cerebrum will not log activity.");
            }

            if (configManager.getOrderChannelId().isEmpty())
            {
                LOGGER.info("Order channel's not been set. Please configure one before proceeding.");
                System.exit(-1);
                return;
            }

            // Set and load UserManager instance.
            userManager = new UserManager(jda);
            // Set and load OrderManager instance.
            orderManager = new OrderManager(jda);
            // Set and load CommandManager instance.
            commandManager = new CommandManager();
            // Set and load CLIManager instance.
            cliManager = new CLIManager();
            // Set and load ReactionManager instance.
            reactionManager = new ReactionManager();

            commandManager.register(new OrderCommand());
            commandManager.register(new LanguageCommand());
            cliManager.registerCommand("stop", new CLIStop());
            cliManager.registerCommand("reload", new CLIReload());

            // Send a log message.
            ComplexEmbed embed = new ComplexEmbed(configManager.getLogChannel());
            embed.setColor(Color.GREEN);
            embed.setTitle(I18N.App.START);
            embed.send();

            // When everything finished loading
            // add a shutdown hook and let live.
            LOGGER.info("Done!");
            LOGGER.info("Press CTRL+C or type \"stop\" to exit.");
            RUNNING = true;

            // Add the shutdown hook (CTRL+C/SIGTERM).
            Runtime.getRuntime().addShutdownHook(new Thread(Cerebrum::exit));

            // While the bot is running...
            while (RUNNING)
            {
                // ...scan user's input for "stop".
                final Scanner scanner = new Scanner(System.in);
                final String input = scanner.nextLine();
                cliManager.fetchAndExecute(input);
            }
        }
        catch (LoginException | InterruptedException ex)
        {
            // Connection failed, log and
            // proceed to terminate the Bot.
            LOGGER.error("Failed to get JDA instance!", ex);
            System.exit(-1);
        }
    }

    /**
     * Reloads {@code Cerebrum}, the configuration and database.
     */
    public static void reload() throws IOException
    {
        LOGGER.info("Reloading Cerebrum...");
        userManager.saveAll();
        configManager.loadConfiguration();
        LOGGER.info("Done, please consider rebooting if you encounter any issues.");
    }

    /**
     * Gracefully exits {@code Cerebrum}.
     */
    private static void exit()
    {
        RUNNING = false;
        // Logging and then stop.
        LOGGER.info("Stopping Cerebrum...");
        userManager.saveAll();
        jda.shutdown();
        LOGGER.info("bye!");
    }

    /**
     * Get {@link JDA}'s instance.
     *
     * @return {@link JDA}'s instance
     */
    public static JDA getJDA()
    {
        return jda;
    }

    /**
     * Get {@link OrderManager}'s instance.
     *
     * @return {@link OrderManager}'s instance
     */
    public static OrderManager getOrderManager()
    {
        return orderManager;
    }

    /**
     * Get {@link UserManager}'s instance.
     *
     * @return {@link UserManager}'s instance
     */
    public static UserManager getUserManager()
    {
        return userManager;
    }

    /**
     * Get {@link ConfigManager}'s instance.
     *
     * @return {@link ConfigManager}'s instance
     */
    public static ConfigManager getConfigManager()
    {
        return configManager;
    }

    /**
     * Get {@link CommandManager}'s instance.
     *
     * @return {@link CommandManager}'s instance
     */
    public static CommandManager getCommandManager() { return commandManager; }

    /**
     * Get {@link CLIManager}'s instance.
     *
     * @return {@link CLIManager}'s instance
     */
    public static CLIManager getCLIManager() { return cliManager; }

    /**
     * Get {@link ReactionManager}'s instance.
     *
     * @return {@link ReactionManager}'s instance
     */
    public static ReactionManager getReactionManager()
    {
        return reactionManager;
    }

    /**
     * Get {@link EventWaiter}'s instance.
     *
     * @return {@link EventWaiter}'s instance
     */
    public static EventWaiter getEventWaiter()
    {
        return eventWaiter;
    }
}

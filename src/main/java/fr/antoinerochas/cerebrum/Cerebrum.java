package fr.antoinerochas.cerebrum;

import fr.antoinerochas.cerebrum.config.ConfigManager;
import fr.antoinerochas.cerebrum.jda.JDAManager;
import fr.antoinerochas.cerebrum.order.OrderManager;
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
     * Log4J's {@link Logger} instance. <p>
     * Used mainly at boot, as separate modules all have a <p>
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
     * Determines if {@code Cerebrum} is in debug mode. <p>
     * Enabling debug will provide additional information <p>
     * about {@code Cerebrum}'s work, requests, activities and much more.
     */
    public static boolean DEBUG = false;

    /**
     * Determines if {@code Cerebrum} is currently running.
     */
    public static boolean RUNNING = false;

    /**
     * The {@link Guild} the server's running on.
     */
    public static Guild GUILD;

    /**
     * The debug flag that will trigger {@code Cerebrum} <p>
     * to enable debugging and set {@link #DEBUG} to {@link Boolean#TRUE}.
     */
    private static final String debugFlag = "-DEBUG";

    /**
     * Represents the current {@link JDA}'s instance <p>
     * {@code Cerebrum} is currently using.
     */
    private static JDA jda;

    /**
     * Java's application entry point.
     *
     * @param args arguments
     */
    public static void main(String[] args) throws IOException
    {
        // Check for the Debug flag.
        if (args.length > 0 && args[0].equals(debugFlag))
        {
            DEBUG = true;
            Configurator.setRootLevel(Level.DEBUG);
            LOGGER.debug("Debug Mode has been enabled. " + APP + " will not be able to take");
            LOGGER.debug("any orders and will use a FakeDB. Proceed carefully (づ｡◕‿‿◕｡)づ !");
            LOGGER.debug("(You can disable Debug Mode by removing the \"-DEBUG\" flag.)");
        }

        // Launch Cerebrum.
        LOGGER.info("Starting " + APP + " " + VERSION + "...");
        LOGGER.info("(c) 2020-present Antoine \"Aro\" ROCHAS.");
        LOGGER.info("Loading Cerebrum...");

        // Instantiate JDAManager.
        JDAManager jdaManager = new JDAManager();
        // Instantiate ConfigManager.
        ConfigManager manager = ConfigManager.loadConfiguration();

        // Try to connect to JDA!
        try
        {
            // Connection succeeded!
            jda = jdaManager.login();
            LOGGER.info("Connection succeeded.");

            // Checking channels validity...
            LOGGER.info("Checking configuration...");

            GUILD = jda.getGuildById(manager.getGuildId());
            if (GUILD == null)
            {
                LOGGER.error("Can't find Guild/Server with ID: " + manager.getGuildId() + "!");
                System.exit(-1);
                return;
            }
            LOGGER.info("Guild found (name=\"" + GUILD.getName() + "\",id=" + manager.getGuildId() + ").");

            // Getting channels from configuration.
            if(manager.getLogChannelId().isEmpty())
            {
                LOGGER.warn("Log channel is not defined. Cerebrum will not log activity.");
            }

            if (manager.getOrderChannelId().isEmpty())
            {
                LOGGER.info("Order channel's not been set. Please configure one before proceeding.");
                System.exit(-1);
                return;
            }

            // Everything's all right !
            LOGGER.info("Done.");
            LOGGER.info("Loading OrderManager...");

            // Instantiate and load OrderManager.
            OrderManager orderManager = new OrderManager(jda);

            // When everything finished loading
            // add a shutdown hook and let live.
            LOGGER.info("Done loading!");
            LOGGER.info("Use CTRL+C or type \"stop\" to quit.");
            RUNNING = true;

            // Add the shutdown hook (CTRL+C/SIGTERM).
            Runtime.getRuntime().addShutdownHook(new Thread(Cerebrum::exit));

            // While the bot is running...
            while (RUNNING)
            {
                // ...scan user's input for "stop".
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                if (input.equals("stop")) { System.exit(0); }
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
     * Exits gracefully {@code Cerebrum}.
     */
    private static void exit()
    {
        // Logging and then stop.
        LOGGER.info("Stopping Cerebrum...");
        jda.shutdown();
    }
}

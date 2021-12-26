package fr.antoinerochas.cerebrum.console;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * This file is part of Cerebrum.
 *
 * @author Aro at/on 02/03/2020
 * @since 1.0
 */
// TODO: 02/03/2020 Documentation.
public class CLIManager {
    /**
     * Log4J's {@link Logger} instance.
     */
    public static final Logger LOGGER = LogManager.getLogger(CLIManager.class);

    /**
     * Represents all the registered commands.
     */
    public static final HashMap<String, CLICommand> COMMANDS = new HashMap<>();

    public void registerCommand(final String label, final CLICommand command) {
        // Put the command into the HashMap.
        COMMANDS.putIfAbsent(label, command);
    }

    public void fetchAndExecute(final String label) {
        // Get the command.
        final CLICommand command = getCommand(label);
        if (command != null) {
            // And execute it.
            command.execute();
            return;
        }

        LOGGER.error("Unknown command \"" + label + "\", type \"help\" for help.");
    }

    public CLICommand getCommand(final String label) {
        // Get the command from the HashMap.
        return COMMANDS.get(label);
    }
}

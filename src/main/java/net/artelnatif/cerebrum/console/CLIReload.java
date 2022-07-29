package net.artelnatif.cerebrum.console;

import net.artelnatif.cerebrum.Cerebrum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * This file is part of Cerebrum.
 *
 * @author Aro at/on 02/03/2020
 * @since 1.0
 */
public class CLIReload implements CLICommand {
    /**
     * Log4J's {@link Logger} instance.
     */
    public static final Logger LOGGER = LogManager.getLogger(CLIReload.class);

    @Override
    public void execute() {
        try {
            Cerebrum.reload();
        } catch (IOException e) {
            LOGGER.error("Failed to reload!", e);
        }
    }
}

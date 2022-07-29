package net.artelnatif.cerebrum.console;

/**
 * This file is part of Cerebrum.
 *
 * @author Aro at/on 02/03/2020
 * @since 1.0
 */
public class CLIStop implements CLICommand {
    @Override
    public void execute() {
        System.exit(0);
    }
}

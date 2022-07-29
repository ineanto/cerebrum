package net.artelnatif.cerebrum.command.framework;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * This file is part of Cerebrum.
 *
 * @author Aro at/on 28/02/2020
 * @since 1.0
 */
record JDACommand(String name, String[] alias, Method method, Object object) {
    @Override
    public String toString() {
        return "JDACommand{" +
                "name='" + name + '\'' +
                ", alias=" + Arrays.toString(alias) +
                '}';
    }
}

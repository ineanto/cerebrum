package net.artelnatif.cerebrum.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Reader;
import java.lang.reflect.Type;

/**
 * This file is part of Cerebrum.
 * Manages all the JSON file I/O
 * using Google's {@link Gson} library.
 *
 * @author Aro at/on 28/01/2020
 * @since 1.0
 */
public class GsonManager {
    /**
     * Log4J's {@link Logger} instance.
     */
    public static final Logger LOGGER = LogManager.getLogger(GsonManager.class);

    /**
     * Google's {@link Gson} instance.
     */
    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    /**
     * Read a JSON file, maps it as {@code T} <p>
     * and return the mapped {@link Object}.
     *
     * @param reader the file's {@link Reader}
     * @param type   the JSON's file {@link Type}
     * @param <T>    the {@link Object}'s type.
     * @return the file as a {@code T} object
     */
    public static <T> T loadFile(Reader reader, Type type) {
        // Return the mapped file as an Object.
        return GSON.fromJson(reader, type);
    }
}

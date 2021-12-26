package fr.antoinerochas.cerebrum.i18n;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;

/**
 * This file is part of Cerebrum.
 * Represent a language and it's associated file.
 *
 * @author Aro at/on 28/01/2020
 * @since 1.0
 */
public enum Language {
    /**
     * The English language.
     */
    ENGLISH("en_EN"),
    /**
     * The French language.
     */
    FRENCH("fr_FR");

    /**
     * Log4J's {@link Logger} instance.
     */
    public final Logger LOGGER = LogManager.getLogger(Language.class);

    /**
     * The language's code.
     * (e.g.: "en_EN"/"fr_FR")
     */
    private String code;

    /**
     * The language's associated file.
     */
    private InputStream stream;

    /**
     * Constructor.
     *
     * @param code the language's code
     */
    Language(String code) {
        // Setting the code.
        this.code = code;
        final String fileName = "/i18n/%s.json".formatted(getCode());
        this.stream = Language.class.getResourceAsStream(fileName);
    }

    /**
     * Get the language's code.
     *
     * @return the language's code
     */
    public String getCode() {
        return code;
    }

    /**
     * Get the language's file stream.
     *
     * @return the language's file stream
     */
    public InputStream getStream() {
        return stream;
    }
}

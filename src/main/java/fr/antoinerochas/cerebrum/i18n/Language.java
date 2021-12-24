package fr.antoinerochas.cerebrum.i18n;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * This file is part of Cerebrum.
 * Represent a language and it's associated file.
 *
 * @author Aro at/on 28/01/2020
 * @since 1.0
 */
public enum Language
{
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
    private File file;

    /**
     * Constructor.
     *
     * @param code the language's code
     */
    Language(String code)
    {
        // Setting the code.
        this.code = code;
        this.file = new File(getClass().getResource(String.format("/resources/i18n/%s.json", getCode())).toExternalForm());
        System.out.println("file.getAbsolutePath() = " + file.getAbsolutePath());
    }

    /**
     * Get the language's code.
     *
     * @return the language's code
     */
    public String getCode()
    {
        return code;
    }

    /**
     * Get the language's code.
     *
     * @return the language's code
     */
    public File getFile()
    {
        return file;
    }
}

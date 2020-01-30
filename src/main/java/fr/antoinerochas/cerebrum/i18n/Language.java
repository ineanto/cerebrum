package fr.antoinerochas.cerebrum.i18n;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

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
        // TODO: 28/01/2020 Needs to be improved/optimised.
        // Find the file.
        URL fileUrl = this.getClass().getClassLoader().getResource("i18n/" + getCode() + ".json");
        try
        {
            // If the URL is not pointing to nothing, create the File object.
            if (fileUrl != null)
            {
                this.file = new File(fileUrl.toURI());
            }
        }
        catch (URISyntaxException ex)
        {
            // If it is, print an error and exit.
            LOGGER.error("Failed to read: " + getCode() + ".json!", ex);
            System.exit(-1);
        }
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

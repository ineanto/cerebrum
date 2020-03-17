package fr.antoinerochas.cerebrum.i18n;

/**
 * This file is part of Cerebrum.
 * Contains all the I18N messages's strings.
 *
 * @author Aro at/on 09/03/2020
 * @since 1.0
 */
public class I18N
{
    /**
     * Application messages.
     */
    public static final class App
    {
        private static final String PREFIX = "app.";

        public static final String START = PREFIX + "start";
        public static final String SHUTDOWN = PREFIX + "shutdown";
    }

    /**
     * Global messages.
     */
    public static final class Global
    {
        private static final String PREFIX = "global.";

        public static final String NULL = PREFIX + "null";
        public static final String SUCCESS = PREFIX + "success";
        public static final String ERROR = PREFIX + "error";
        public static final String ERROR_DESC = PREFIX + "error.desc";
    }

    /**
     * Strings for messages.
     */
    public static final class Messages
    {
        private static final String MSG_PREFIX = "msg.";

        public static final class Order
        {
            private static final String PREFIX = MSG_PREFIX + "order.";

            public static final String NOT_AVAILABLE = PREFIX + "notavailable";
            public static final String ALREADY_ORDERING = PREFIX + "alreadyordering";
            public static final String SUCCESS_DESC = PREFIX + "success.desc";
            public static final String PROGRESS = PREFIX + "progress";
            public static final String UPDATE_DESC = PREFIX + "update.desc";
            public static final String UPDATE_MSG = PREFIX + "update.msg";
            public static final String TYPE_DESC = PREFIX + "type.desc";
            public static final String TYPE_MSG = PREFIX + "type.msg";
            public static final String DESCRIPTION_DESC = PREFIX + "description.desc";
            public static final String DESCRIPTION_MSG = PREFIX + "description.msg";
            public static final String PRICE_DESC = PREFIX + "price.desc";
            public static final String PRICE_MSG = PREFIX + "price.msg";
            public static final String PRICE_ERROR = PREFIX + "price.error";
        }

        public static final class Language
        {
            private static final String PREFIX = "msg.i18n.";

            public static final String TITLE = PREFIX + "title";
            public static final String DESC = PREFIX + "desc";
            public static final String MESSAGE = PREFIX + "msg";
            public static final String UPDATE_MSG = PREFIX + "update.msg";
        }
    }
}

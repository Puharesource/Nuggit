package io.puharesource.mc.nuggit.backend;

import io.puharesource.mc.nuggit.Nuggit;

import java.util.logging.Logger;

public final class Log {
    private final static Logger LOGGER = Logger.getLogger("Nuggit");

    private static String constructText(final String text) {
        return "[Debug] " + text;
    }

    public static void info(final String text) {
        if (Nuggit.getInstance().getMainConfig().debug)
            LOGGER.info(constructText(text));
    }

    public static void warning(final String text) {
        LOGGER.warning(constructText(text));
    }

    public static void severe(final String text) {
        LOGGER.severe(constructText(text));
    }
}

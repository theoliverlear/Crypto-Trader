package org.cryptotrader.logging.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;

/**
 * Strips ANSI escape codes from log messages.
 * This ensures that log files remain clean even when console colors are enabled.
 */
public class AnsiStripperConverter extends CompositeConverter<ILoggingEvent> {
    @Override
    protected String transform(ILoggingEvent event, String in) {
        if (in == null) {
            return null;
        }

        final String colorCodeRegex = "\u001B\\[[;\\d]*m";
        return in.replaceAll(colorCodeRegex, "");
    }
}

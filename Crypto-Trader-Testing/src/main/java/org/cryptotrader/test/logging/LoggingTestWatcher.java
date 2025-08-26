package org.cryptotrader.test.logging;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class LoggingTestWatcher implements TestWatcher {
    private static final Logger log = LoggerFactory.getLogger(LoggingTestWatcher.class);

    // ANSI colors
    private static final String RESET  = "\u001B[0m";
    private static final String GREEN  = "\u001B[32m";
    private static final String RED    = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE   = "\u001B[34m";

    @Override
    public void testSuccessful(ExtensionContext context) {
        log.info("{}✓ TEST PASSED{}: {}.{}()",
                GREEN, RESET,
                context.getRequiredTestClass().getSimpleName(),
                context.getRequiredTestMethod().getName());
        this.logSeparator();
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        log.error("{}✗ TEST FAILED{}: {}.{}() - {}",
                RED, RESET,
                context.getRequiredTestClass().getSimpleName(),
                context.getRequiredTestMethod().getName(),
                cause.toString());
        this.logSeparator();
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        log.warn("{}⚠ TEST DISABLED{}: {}.{}() - {}",
                YELLOW, RESET,
                context.getRequiredTestClass().getSimpleName(),
                context.getRequiredTestMethod().getName(),
                reason.orElse("no reason"));
        this.logSeparator();
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        log.warn("{}ℹ TEST ABORTED{}: {}.{}() - {}",
                BLUE, RESET,
                context.getRequiredTestClass().getSimpleName(),
                context.getRequiredTestMethod().getName(),
                cause.toString());
        this.logSeparator();
    }
    
    public void logSeparator() {
        log.info("-".repeat(55));
    }
}
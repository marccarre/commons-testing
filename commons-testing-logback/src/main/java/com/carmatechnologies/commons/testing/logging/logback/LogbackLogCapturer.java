package com.carmatechnologies.commons.testing.logging.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.core.Appender;
import com.carmatechnologies.commons.testing.logging.api.ILogCapturer;
import org.slf4j.Logger;

import static com.carmatechnologies.commons.testing.utils.Preconditions.checkNotNull;

public class LogbackLogCapturer implements ILogCapturer {
    private final Appender appender;
    private final ch.qos.logback.classic.Logger logger;
    private final boolean initialAdditivity;
    private final Level initialLevel;
    private final boolean isAdditive;
    private final Level level;

    public LogbackLogCapturer(final ch.qos.logback.classic.Logger logbackLogger, final Appender appender, final boolean isAdditive, final Level level) {
        this.appender = checkNotNull(appender, "Appender must NOT be null");
        this.logger = checkNotNull(logbackLogger, "Logback logger must NOT be null");
        this.initialAdditivity = logger.isAdditive();
        this.initialLevel = logger.getLevel();
        this.isAdditive = isAdditive;
        this.level = checkNotNull(level, "Level must NOT be null.");
    }

    public LogbackLogCapturer(final Logger slf4jLogger, final Appender appender, final boolean isAdditive, final Level level) {
        this(getLogger(checkNotNull(slf4jLogger, "SLF4J logger must NOT be null")), appender, isAdditive, level);
    }

    private static ch.qos.logback.classic.Logger getLogger(final Logger slf4jLogger) {
        if (!(slf4jLogger instanceof ch.qos.logback.classic.Logger))
            throw new IllegalArgumentException("SLF4J logger must be a Logback logger, but was: " + slf4jLogger.getClass().getCanonicalName());
        try {
            return (ch.qos.logback.classic.Logger) slf4jLogger;
        } catch (Exception e) {
            throw new RuntimeException("Failed to enable log capture for Logback", e);
        }
    }

    @Override
    public void enableLogCapture() {
        synchronized (logger) {
            logger.setAdditive(isAdditive);
            logger.setLevel(level);
            logger.addAppender(appender);
        }
    }

    @Override
    public void disableLogCapture() {
        synchronized (logger) {
            logger.detachAppender(appender);
            logger.setLevel(initialLevel);
            logger.setAdditive(initialAdditivity);
        }
    }
}

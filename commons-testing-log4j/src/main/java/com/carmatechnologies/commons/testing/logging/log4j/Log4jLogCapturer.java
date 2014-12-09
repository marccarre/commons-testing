package com.carmatechnologies.commons.testing.logging.log4j;

import com.carmatechnologies.commons.testing.logging.api.ILogCapturer;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.impl.Log4jLoggerAdapter;

import java.lang.reflect.Field;

import static com.carmatechnologies.commons.testing.utils.Preconditions.checkNotNull;

public class Log4jLogCapturer implements ILogCapturer {
    private static final String LOGGER_FIELD_NAME = "logger";
    private final Appender appender;
    private final org.apache.log4j.Logger logger;
    private final boolean initialAdditivity;
    private final Level initialLevel;
    private final boolean isAdditive;
    private final Level level;

    public Log4jLogCapturer(final org.apache.log4j.Logger log4jLogger, final Appender appender, final boolean isAdditive, final Level level) {
        this.appender = checkNotNull(appender, "Appender must NOT be null");
        this.logger = checkNotNull(log4jLogger, "Log4j logger must NOT be null");
        this.initialAdditivity = logger.getAdditivity();
        this.initialLevel = logger.getLevel();
        this.isAdditive = isAdditive;
        this.level = checkNotNull(level, "Level must NOT be null.");
    }

    public Log4jLogCapturer(final Logger slf4jLogger, final Appender appender, final boolean isAdditive, final Level level) {
        this(getLogger(checkNotNull(slf4jLogger, "SLF4J logger must NOT be null")), appender, isAdditive, level);
    }

    private static org.apache.log4j.Logger getLogger(final Logger slf4jLogger) {
        if (!(slf4jLogger instanceof Log4jLoggerAdapter))
            throw new IllegalArgumentException("SLF4J logger must be a Log4j logger, but was: " + slf4jLogger.getClass().getCanonicalName());
        try {
            return tryGetLogger((Log4jLoggerAdapter) slf4jLogger);
        } catch (Exception e) {
            throw new RuntimeException("Failed to enable log capture for Log4J", e);
        }
    }

    private static org.apache.log4j.Logger tryGetLogger(final Log4jLoggerAdapter slf4jLogger) throws NoSuchFieldException, IllegalAccessException {
        final Field field = Log4jLoggerAdapter.class.getDeclaredField(LOGGER_FIELD_NAME);
        field.setAccessible(true);
        return (org.apache.log4j.Logger) field.get(slf4jLogger);
    }

    @Override
    public void enableLogCapture() {
        synchronized (logger) {
            logger.setAdditivity(isAdditive);
            logger.setLevel(level);
            logger.addAppender(appender);
        }
    }

    @Override
    public void disableLogCapture() {
        synchronized (logger) {
            logger.removeAppender(appender);
            logger.setLevel(initialLevel);
            logger.setAdditivity(initialAdditivity);
        }
    }
}

package com.carmatechnologies.commons.testing.logging.log4j2;

import com.carmatechnologies.commons.testing.logging.api.ILogCapturer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.slf4j.Log4jLogger;
import org.slf4j.Logger;

import java.lang.reflect.Field;

import static com.carmatechnologies.commons.testing.utils.Preconditions.checkNotNull;

public class Log4j2LogCapturer implements ILogCapturer {
    private static final String LOGGER_FIELD_NAME = "logger";
    private final Appender appender;
    private final org.apache.logging.log4j.core.Logger logger;
    private final boolean initialAdditivity;
    private final Level initialLevel;
    private final boolean isAdditive;
    private final Level level;

    public Log4j2LogCapturer(final org.apache.logging.log4j.core.Logger log4j2Logger, final Appender appender, final boolean isAdditive, final Level level) {
        this.appender = checkNotNull(appender, "Appender must NOT be null");
        this.logger = checkNotNull(log4j2Logger, "Log4j2 logger must NOT be null");
        this.initialAdditivity = logger.isAdditive();
        this.initialLevel = logger.getLevel();
        this.isAdditive = isAdditive;
        this.level = checkNotNull(level, "Level must NOT be null.");
    }

    public Log4j2LogCapturer(final Logger slf4jLogger, final Appender appender, final boolean isAdditive, final Level level) {
        this(getLogger(checkNotNull(slf4jLogger, "SLF4J logger must NOT be null")), appender, isAdditive, level);
    }

    private static org.apache.logging.log4j.core.Logger getLogger(final Logger slf4jLogger) {
        if (!(slf4jLogger instanceof Log4jLogger))
            throw new IllegalArgumentException("SLF4J logger must be a Log4j2 logger, but was: " + slf4jLogger.getClass().getCanonicalName());
        try {
            return tryGetLogger((Log4jLogger) slf4jLogger);
        } catch (Exception e) {
            throw new RuntimeException("Failed to enable log capture for Log4j2", e);
        }
    }

    private static org.apache.logging.log4j.core.Logger tryGetLogger(final Log4jLogger slf4jLogger) throws NoSuchFieldException, IllegalAccessException {
        final Field field = Log4jLogger.class.getDeclaredField(LOGGER_FIELD_NAME);
        field.setAccessible(true);
        return (org.apache.logging.log4j.core.Logger) field.get(slf4jLogger);
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
            logger.removeAppender(appender);
            logger.setLevel(initialLevel);
            logger.setAdditive(initialAdditivity);
        }
    }
}

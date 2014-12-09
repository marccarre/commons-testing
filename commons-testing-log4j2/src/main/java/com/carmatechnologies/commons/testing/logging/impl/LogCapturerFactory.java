package com.carmatechnologies.commons.testing.logging.impl;

import com.carmatechnologies.commons.testing.logging.api.ILogCapturer;
import com.carmatechnologies.commons.testing.logging.api.ILogCapturerFactory;
import com.carmatechnologies.commons.testing.logging.api.LogLevel;
import com.carmatechnologies.commons.testing.logging.log4j2.Log4j2CollectionAppender;
import com.carmatechnologies.commons.testing.logging.log4j2.Log4j2LogCapturer;
import org.apache.logging.log4j.Level;
import org.apache.logging.slf4j.Log4jLogger;
import org.slf4j.Logger;

import java.util.Collection;

import static com.carmatechnologies.commons.testing.utils.Preconditions.checkNotNull;

public class LogCapturerFactory extends AbstractLogCapturerFactory implements ILogCapturerFactory {

    @Override
    protected boolean bindsToLoggingFramework(final Logger logger) {
        return (logger instanceof Log4jLogger);
    }

    @Override
    protected String getName() {
        return Log4jLogger.class.getName();
    }

    @Override
    protected ILogCapturer newLogCapturer(final Logger slf4jLogger, final LogLevel logLevel, final boolean isAdditive, final Collection<String> logs) {
        return new Log4j2LogCapturer(slf4jLogger, new Log4j2CollectionAppender(logs), isAdditive, getLevel(logLevel));
    }

    private static Level getLevel(final LogLevel logLevel) {
        checkNotNull(logLevel, "Log level must NOT be null.");
        switch (logLevel) {
            case TRACE:
                return Level.TRACE;
            case DEBUG:
                return Level.DEBUG;
            case INFO:
                return Level.INFO;
            case WARN:
                return Level.WARN;
            case ERROR:
                return Level.ERROR;
            default:
                throw new UnsupportedOperationException("No mapping from [" + logLevel + "] to [" + Level.class.getName() + "].");
        }
    }
}

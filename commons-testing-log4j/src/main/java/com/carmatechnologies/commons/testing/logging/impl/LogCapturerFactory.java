package com.carmatechnologies.commons.testing.logging.impl;

import com.carmatechnologies.commons.testing.logging.api.ILogCapturer;
import com.carmatechnologies.commons.testing.logging.api.ILogCapturerFactory;
import com.carmatechnologies.commons.testing.logging.api.LogLevel;
import com.carmatechnologies.commons.testing.logging.log4j.Log4jCollectionAppender;
import com.carmatechnologies.commons.testing.logging.log4j.Log4jLogCapturer;
import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.impl.Log4jLoggerAdapter;

import java.util.Collection;

import static com.carmatechnologies.commons.testing.utils.Preconditions.checkNotNull;

public class LogCapturerFactory extends AbstractLogCapturerFactory implements ILogCapturerFactory {

    @Override
    protected boolean bindsToLoggingFramework(final Logger logger) {
        return (logger instanceof Log4jLoggerAdapter);
    }

    @Override
    protected String getName() {
        return Log4jLoggerAdapter.class.getName();
    }

    @Override
    protected ILogCapturer newLogCapturer(final Logger slf4jLogger, final LogLevel logLevel, final boolean isAdditive, final Collection<String> logs) {
        return new Log4jLogCapturer(slf4jLogger, new Log4jCollectionAppender(logs), isAdditive, getLevel(logLevel));
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

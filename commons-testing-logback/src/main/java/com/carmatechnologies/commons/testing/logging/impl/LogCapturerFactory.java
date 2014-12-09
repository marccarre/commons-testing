package com.carmatechnologies.commons.testing.logging.impl;

import ch.qos.logback.classic.Level;
import com.carmatechnologies.commons.testing.logging.api.ILogCapturer;
import com.carmatechnologies.commons.testing.logging.api.ILogCapturerFactory;
import com.carmatechnologies.commons.testing.logging.api.LogLevel;
import com.carmatechnologies.commons.testing.logging.logback.LogbackCollectionAppender;
import com.carmatechnologies.commons.testing.logging.logback.LogbackLogCapturer;
import org.slf4j.Logger;

import java.util.Collection;

import static com.carmatechnologies.commons.testing.utils.Preconditions.checkNotNull;

public class LogCapturerFactory extends AbstractLogCapturerFactory implements ILogCapturerFactory {

    @Override
    protected boolean bindsToLoggingFramework(final Logger logger) {
        return (logger instanceof ch.qos.logback.classic.Logger);
    }

    @Override
    protected String getName() {
        return ch.qos.logback.classic.Logger.class.getName();
    }

    @Override
    protected ILogCapturer newLogCapturer(final Logger slf4jLogger, final LogLevel logLevel, final boolean isAdditive, final Collection<String> logs) {
        return new LogbackLogCapturer(slf4jLogger, new LogbackCollectionAppender(logs), isAdditive, getLevel(logLevel));
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

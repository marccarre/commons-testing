package com.carmatechnologies.commons.testing.logging.impl;

import com.carmatechnologies.commons.testing.logging.api.ILogCapturer;
import com.carmatechnologies.commons.testing.logging.api.ILogCapturerFactory;
import com.carmatechnologies.commons.testing.logging.api.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractLogCapturerFactory implements ILogCapturerFactory {
    private final ConcurrentMap<String, ILogCapturer> capturers = new ConcurrentHashMap<>();

    @Override
    public ILogCapturer createFor(final Class<?> type, final LogLevel logLevel, final boolean isAdditive, final Collection<String> logs) {
        final Logger slf4jLogger = getLogger(type);
        if (bindsToLoggingFramework(slf4jLogger)) {
            return getLogCapturer(slf4jLogger, type, logLevel, isAdditive, logs);
        } else {
            throw new RuntimeException("No appropriate adapter available to capture logs via " + getName());
        }
    }

    private static Logger getLogger(final Class<?> type) {
        synchronized (LoggerFactory.class) {
            return LoggerFactory.getLogger(type);
        }
    }

    protected abstract boolean bindsToLoggingFramework(final Logger logger);

    private ILogCapturer getLogCapturer(final Logger slf4jLogger, final Class<?> type, final LogLevel logLevel, final boolean isAdditive, final Collection<String> logs) {
        final String typeName = type.getName();

        final ILogCapturer capturer = capturers.get(typeName);
        if (capturer != null)
            return capturer;

        final ILogCapturer newCapturer = newLogCapturer(slf4jLogger, logLevel, isAdditive, logs);
        final ILogCapturer oldCapturer = capturers.putIfAbsent(typeName, newCapturer);
        return (oldCapturer == null) ? newCapturer : oldCapturer;
    }

    protected abstract ILogCapturer newLogCapturer(final Logger slf4jLogger, final LogLevel logLevel, final boolean isAdditive, final Collection<String> logs);

    protected abstract String getName();
}

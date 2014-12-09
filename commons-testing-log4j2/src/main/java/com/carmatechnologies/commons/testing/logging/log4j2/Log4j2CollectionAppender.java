package com.carmatechnologies.commons.testing.logging.log4j2;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.ErrorHandler;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

import static com.carmatechnologies.commons.testing.utils.Preconditions.checkNotNull;

public class Log4j2CollectionAppender implements Appender {
    private static final AtomicLong counter = new AtomicLong(0L);
    private volatile boolean isStarted = true;

    private final Collection<String> logs;
    private final String name;

    public Log4j2CollectionAppender(final Collection<String> logs) {
        this.logs = checkNotNull(logs, "Log statements queue must NOT be null.");
        this.name = Log4j2CollectionAppender.class.getCanonicalName() + "-" + counter.incrementAndGet();
    }

    @Override
    public void append(final LogEvent logEvent) {
        logs.add(logEvent.getMessage().getFormattedMessage());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Layout<? extends Serializable> getLayout() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean ignoreExceptions() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public ErrorHandler getHandler() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void setHandler(final ErrorHandler errorHandler) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void start() {
        isStarted = true;
    }

    @Override
    public void stop() {
        isStarted = false;
    }

    @Override
    public boolean isStarted() {
        return isStarted;
    }

    @Override
    public boolean isStopped() {
        return !isStarted;
    }
}

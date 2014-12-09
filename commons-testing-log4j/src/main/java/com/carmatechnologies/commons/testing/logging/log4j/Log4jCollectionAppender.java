package com.carmatechnologies.commons.testing.logging.log4j;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

import static com.carmatechnologies.commons.testing.utils.Preconditions.checkNotNull;

public class Log4jCollectionAppender implements Appender {
    private static final AtomicLong counter = new AtomicLong(0L);

    private final Collection<String> logs;
    private final String name;

    public Log4jCollectionAppender(final Collection<String> logs) {
        this.logs = checkNotNull(logs, "Log statements queue must NOT be null.");
        this.name = Log4jCollectionAppender.class.getCanonicalName() + "-" + counter.incrementAndGet();
    }

    @Override
    public void addFilter(final Filter filter) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Filter getFilter() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void clearFilters() {
        // Nothing to clear.
    }

    @Override
    public void close() {
        // Nothing to close.
    }

    @Override
    public void doAppend(final LoggingEvent loggingEvent) {
        logs.add(loggingEvent.getMessage().toString());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setErrorHandler(final ErrorHandler errorHandler) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public ErrorHandler getErrorHandler() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void setLayout(final Layout layout) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Layout getLayout() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void setName(final String name) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}

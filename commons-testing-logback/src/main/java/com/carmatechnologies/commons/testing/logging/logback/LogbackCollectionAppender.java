package com.carmatechnologies.commons.testing.logging.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.LogbackException;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.Status;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.carmatechnologies.commons.testing.utils.Preconditions.checkNotNull;

public class LogbackCollectionAppender implements Appender<ILoggingEvent> {
    private static final AtomicLong counter = new AtomicLong(0L);

    private final Collection<String> logs;
    private final String name;

    private volatile boolean isStarted = true;

    public LogbackCollectionAppender(final Collection<String> logs) {
        this.logs = checkNotNull(logs, "Log statements queue must NOT be null.");
        this.name = LogbackCollectionAppender.class.getCanonicalName() + "-" + counter.incrementAndGet();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void doAppend(final ILoggingEvent loggingEvent) throws LogbackException {
        logs.add(loggingEvent.getFormattedMessage());
    }

    @Override
    public void setName(final String name) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void setContext(final Context context) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Context getContext() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void addStatus(final Status status) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void addInfo(final String s) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void addInfo(final String s, final Throwable throwable) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void addWarn(final String s) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void addWarn(final String s, final Throwable throwable) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void addError(final String s) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void addError(final String s, final Throwable throwable) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void addFilter(final Filter<ILoggingEvent> filter) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void clearAllFilters() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<Filter<ILoggingEvent>> getCopyOfAttachedFiltersList() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public FilterReply getFilterChainDecision(final ILoggingEvent iLoggingEvent) {
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
}

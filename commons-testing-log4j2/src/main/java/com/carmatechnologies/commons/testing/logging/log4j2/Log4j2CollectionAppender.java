package com.carmatechnologies.commons.testing.logging.log4j2;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.ErrorHandler;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

import static com.carmatechnologies.commons.testing.utils.Preconditions.checkNotNull;

public class Log4j2CollectionAppender extends AbstractAppender implements Appender {
    private static final AtomicLong counter = new AtomicLong(0L);
    private volatile State state = State.INITIALIZING;
    private final Collection<String> logs;

    public Log4j2CollectionAppender(final Collection<String> logs) {
        super(
                Log4j2CollectionAppender.class.getCanonicalName() + "-" + counter.incrementAndGet(),
                null, null, true, null
        );
        this.logs = checkNotNull(logs, "Log statements queue must NOT be null.");
    }

    @Override
    public void append(final LogEvent logEvent) {
        logs.add(logEvent.getMessage().getFormattedMessage());
    }

    @Override
    public void initialize() {
        state = State.INITIALIZED;
    }

    @Override
    public void start() {
        state = State.STARTED;
    }

    @Override
    public void stop() {
        state = State.STOPPED;
    }

    @Override
    public boolean isStarted() {
        return state == State.STARTED;
    }

    @Override
    public boolean isStopped() {
        return state == State.STOPPED;
    }
}

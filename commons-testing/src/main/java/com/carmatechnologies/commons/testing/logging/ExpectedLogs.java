package com.carmatechnologies.commons.testing.logging;

import com.carmatechnologies.commons.testing.logging.api.ILogCapturer;
import com.carmatechnologies.commons.testing.logging.api.ILogCapturerFactory;
import com.carmatechnologies.commons.testing.logging.api.LogLevel;
import com.carmatechnologies.commons.testing.logging.impl.Binder;
import com.carmatechnologies.commons.testing.logging.junit.CompositeILogStatement;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.Collections;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.carmatechnologies.commons.testing.utils.Preconditions.checkArgument;
import static com.carmatechnologies.commons.testing.utils.Preconditions.checkNotNull;

public class ExpectedLogs implements TestRule, Iterable<String> {
    private final ILogCapturerFactory factory = Binder.createLogCapturerFactory();

    private final Set<ILogCapturer> logCapturers = Collections.newSetFromMap(new ConcurrentHashMap<ILogCapturer, Boolean>());
    private final Queue<String> logs = new ConcurrentLinkedQueue<String>();

    private LogLevel defaultLevel = LogLevel.DEBUG;
    private boolean defaultAdditivity = true;
    private Iterator<String> iterator;

    @Override
    public Statement apply(final Statement statement, final Description description) {
        return new CompositeILogStatement(statement, logCapturers);
    }

    public void setLevel(final LogLevel logLevel) {
        checkNotNull(logLevel, "%s must NOT be null.", LogLevel.class.getSimpleName());
        this.defaultLevel = logLevel;
    }

    public void setAdditivity(final boolean isAdditive) {
        this.defaultAdditivity = isAdditive;
    }

    public void captureFor(final Class<?> type, final LogLevel logLevel, final boolean isAdditive) {
        logCapturers.add(factory.createFor(type, logLevel, isAdditive, logs));
    }

    public void captureFor(final Class<?> type, final LogLevel logLevel) {
        captureFor(type, logLevel, defaultAdditivity);
    }

    public void captureFor(final Class<?> type) {
        captureFor(type, defaultLevel, defaultAdditivity);
    }

    public boolean isEmpty() {
        return logs.isEmpty();
    }

    public int size() {
        return logs.size();
    }

    public boolean contains(final String expectedLog) {
        for (final String log : logs)
            if (log.contains(expectedLog))
                return true;
        return false;
    }

    /**
     * WARNING: Does not use direct access, i.e. O(1), but iterate over all items: O(n).
     */
    public String get(final int index) {
        final int size = size();
        checkArgument(index < size, "[%d] log messages have been captured. Please provide an index such that [%d < %d].", size, index, size);
        int i = 0;
        for (final String log : logs) {
            if (i == index)
                return log;
            if (i > index)
                return null;
            i++;
        }
        return null;
    }

    public String next() {
        if (iterator == null)
            iterator = iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    @Override
    public Iterator<String> iterator() {
        return logs.iterator();
    }
}

package com.carmatechnologies.commons.testing.logging;

import com.carmatechnologies.commons.testing.logging.api.LogLevel;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.carmatechnologies.commons.testing.matchers.ThrowableMatcher.threw;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class ExpectedLogsTest {
    @Rule
    public final ExpectedLogs logs = new ExpectedLogs() {{
        captureFor(Incrementer.class, LogLevel.INFO);
    }};

    @Test
    public void expectedLogsShouldBeEmpty() {
        assertThat(logs.isEmpty(), is(true));
        assertThat(logs.size(), is(0));
    }

    @Test
    public void infoLevelLogsShouldBeCapturedAndAccessibleViaAllApiMethods() {
        Incrementer incrementer = new Incrementer();
        assertThat(incrementer.increment(1), is(2));

        assertThat(logs.isEmpty(), is(false));
        assertThat(logs.size(), is(1));

        assertThat(logs.get(0), is("Incremented 1 by one and returned 2."));
        assertThat(logs.contains("returned 2."), is(true));
        assertThat(logs.next(), is("Incremented 1 by one and returned 2."));
        assertThat(logs.next(), is(nullValue()));
    }

    @Test
    public void expectedLogsShouldExposeLogsIterator() {
        Incrementer incrementer = new Incrementer();
        assertThat(incrementer.increment(1), is(2));

        assertThat(logs.isEmpty(), is(false));
        assertThat(logs.size(), is(1));

        final Iterator<String> iterator = logs.iterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is("Incremented 1 by one and returned 2."));
        assertThat(iterator.hasNext(), is(false));
        assertThat(new Runnable() {
            @Override
            public void run() {
                iterator.next();
            }
        }, threw(NoSuchElementException.class));
    }

    private static class Incrementer {
        private static Logger logger = LoggerFactory.getLogger(Incrementer.class);

        public int increment(final int i) {
            logger.debug("i=" + i);
            final int j = i + 1;
            logger.info("Incremented " + i + " by one and returned " + j + ".");
            logger.debug("j=" + j);
            return j;
        }
    }
}

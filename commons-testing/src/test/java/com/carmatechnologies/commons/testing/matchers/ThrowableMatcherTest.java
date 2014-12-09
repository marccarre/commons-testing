package com.carmatechnologies.commons.testing.matchers;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.carmatechnologies.commons.testing.matchers.ThrowableMatcher.threw;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class ThrowableMatcherTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void threwShouldHaveCaughtAndAssertedException() {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                throw new IllegalArgumentException();
            }
        };

        assertThat(runnable, threw(IllegalArgumentException.class));
    }

    @Test
    public void threwDifferentExceptionTypeShouldFailAndThrowAssertionErrorWithMeaningfulDescription() {
        exception.expect(AssertionError.class);
        exception.expectMessage(containsString("Expected: Should have thrown java.lang.IllegalArgumentException but threw java.lang.NullPointerException."));

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                throw new NullPointerException();
            }
        };

        assertThat(runnable, threw(IllegalArgumentException.class));
    }

    @Test
    public void threwNothingShouldFailAndThrowAssertionErrorWithMeaningfulDescription() {
        exception.expect(AssertionError.class);
        exception.expectMessage(containsString("Expected: Should have thrown java.lang.IllegalArgumentException but threw nothing."));

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
            }
        };

        assertThat(runnable, threw(IllegalArgumentException.class));
    }
}

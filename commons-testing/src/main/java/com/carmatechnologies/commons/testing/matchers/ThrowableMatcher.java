package com.carmatechnologies.commons.testing.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ThrowableMatcher extends TypeSafeMatcher<Runnable> {
    private final String expected;
    private String actual = "nothing";

    @Factory
    public static Matcher<Runnable> threw(final Class<? extends Throwable> expected) {
        return new ThrowableMatcher(expected);
    }

    ThrowableMatcher(final Class<? extends Throwable> expected) {
        this.expected = expected.getName();
    }

    @Override
    protected boolean matchesSafely(final Runnable action) {
        try {
            action.run();
            return false;
        } catch (Throwable t) {
            actual = t.getClass().getName();
            return expected.equals(actual);
        }
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("Should have thrown ").appendText(expected).appendText(" but threw ").appendText(actual).appendText(".");
    }
}

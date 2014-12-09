package com.carmatechnologies.commons.testing.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.carmatechnologies.commons.testing.utils.Preconditions.checkArgument;
import static com.carmatechnologies.commons.testing.utils.Preconditions.checkNotNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PreconditionsTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void checkNonNullObjectShouldReturnIt() {
        assertThat(checkNotNull("Blah", "Blah must NOT be null."), is("Blah"));
    }

    @Test
    public void checkNullObjectShouldThrowException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(equalTo("Blah must NOT be null."));
        checkNotNull(null, "Blah must NOT be null.");
    }

    @Test
    public void checkingArgumentForOneEqualOneShouldNotThrowAnyException() {
        checkArgument(1 == 1, "1 did NOT equal 1???");
    }

    @Test
    public void checkingArgumentForOneEqualTwoShouldThrowException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(equalTo("1 did NOT equal 2!"));
        checkArgument(1 == 2, "1 did NOT equal 2!");
    }
}

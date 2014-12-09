package com.carmatechnologies.commons.testing.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.carmatechnologies.commons.testing.utils.Postconditions.require;
import static org.hamcrest.CoreMatchers.equalTo;

public class PostconditionsTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void requiringOneEqualOneShouldNotThrowAnyException() {
        require(1 == 1, "1 did NOT equal 1???");
    }

    @Test
    public void requiringOneEqualTwoShouldThrowException() {
        exception.expect(IllegalStateException.class);
        exception.expectMessage(equalTo("1 did NOT equal 2!"));
        require(1 == 2, "1 did NOT equal 2!");
    }
}

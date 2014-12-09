package com.carmatechnologies.commons.testing.utils;

public final class Postconditions {
    private Postconditions() {
        // Pure utility class, do NOT instantiate.
    }

    public static void require(final boolean condition, final String message) {
        if (!condition)
            throw new IllegalStateException(message);
    }
}

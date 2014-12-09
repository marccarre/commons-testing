package com.carmatechnologies.commons.testing.utils;

public final class Preconditions {
    private Preconditions() {
        // Pure utility class, do NOT instantiate.
    }

    public static <T> T checkNotNull(final T object, final String message) {
        if (object == null)
            throw new IllegalArgumentException(message);
        return object;
    }

    public static <T> T checkNotNull(final T object, final String messageTemplate, final Object... messageArgs) {
        if (object == null)
            throw new IllegalArgumentException(String.format(messageTemplate, messageArgs));
        return object;
    }

    public static void checkArgument(final boolean condition, final String message) {
        if (!condition)
            throw new IllegalArgumentException(message);
    }

    public static void checkArgument(final boolean condition, final String messageTemplate, final Object... messageArgs) {
        if (!condition)
            throw new IllegalArgumentException(String.format(messageTemplate, messageArgs));
    }
}

package com.carmatechnologies.commons.testing.logging.api;

import java.util.Collection;

public interface ILogCapturerFactory {
    ILogCapturer createFor(final Class<?> type, final LogLevel logLevel, final boolean isAdditive, final Collection<String> logs);

    public static final String LOG_CAPTURER_FACTORY_CLASS_PATH = "com/carmatechnologies/commons/testing/logging/impl/LogCapturerFactory.class";
    public static final String LOG_CAPTURER_FACTORY_CLASS_NAME = "com.carmatechnologies.commons.testing.logging.impl.LogCapturerFactory";
}

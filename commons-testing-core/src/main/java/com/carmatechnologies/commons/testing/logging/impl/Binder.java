package com.carmatechnologies.commons.testing.logging.impl;

import com.carmatechnologies.commons.testing.logging.api.ILogCapturerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.carmatechnologies.commons.testing.utils.Postconditions.require;

public final class Binder {
    private Binder() {
        // Pure utility class, do NOT instantiate.
    }

    public static ILogCapturerFactory createLogCapturerFactory() {
        try {
            return tryCreateLogCapturerFactory();
        } catch (IOException e) {
            throw new RuntimeException("Failed to get resources from classpath. ", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to get implementation class for [" + ILogCapturerFactory.class.getSimpleName() + "].", e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Failed to instantiate [" + ILogCapturerFactory.class.getSimpleName() + "] object.", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unexpected error when trying to bind implementation to [" + ILogCapturerFactory.class.getSimpleName() + "].", e);
        }
    }

    private static ILogCapturerFactory tryCreateLogCapturerFactory() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        final Set<URL> paths = findLogCapturerFactoryInClasspath();
        require(!paths.isEmpty(), "Could not find any binding for [" + ILogCapturerFactory.class.getSimpleName() + "] on classpath. Make sure you make one available.");
        require(paths.size() == 1, "Found multiple bindings for [" + ILogCapturerFactory.class.getSimpleName() + "] on classpath. Make sure you make ONLY one available: \n" + toMessage(paths));
        return (ILogCapturerFactory) Class.forName(ILogCapturerFactory.LOG_CAPTURER_FACTORY_CLASS_NAME).newInstance();
    }

    private static Set<URL> findLogCapturerFactoryInClasspath() throws IOException {
        final Set<URL> paths = new LinkedHashSet<>();
        final Enumeration<URL> resources = scanClassPath();
        while (resources.hasMoreElements()) {
            paths.add(resources.nextElement());
        }
        return paths;
    }

    private static Enumeration<URL> scanClassPath() throws IOException {
        final ClassLoader classLoader = Binder.class.getClassLoader();
        if (classLoader == null) {
            return ClassLoader.getSystemResources(ILogCapturerFactory.LOG_CAPTURER_FACTORY_CLASS_PATH);
        } else {
            return classLoader.getResources(ILogCapturerFactory.LOG_CAPTURER_FACTORY_CLASS_PATH);
        }
    }

    private static String toMessage(final Set<URL> urls) {
        final StringBuilder builder = new StringBuilder();
        for (final URL url : urls) {
            builder.append("\t");
            builder.append("- Found binding in [");
            builder.append(url);
            builder.append("].\n");
        }
        return builder.toString();
    }
}

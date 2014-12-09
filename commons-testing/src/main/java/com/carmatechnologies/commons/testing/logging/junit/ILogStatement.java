package com.carmatechnologies.commons.testing.logging.junit;

import com.carmatechnologies.commons.testing.logging.api.ILogCapturer;
import org.junit.runners.model.Statement;

import static com.carmatechnologies.commons.testing.utils.Preconditions.checkNotNull;

/**
 * Decorator around {@link org.junit.runners.model.Statement} which:
 * - 1) enable log capture (to implement in child classes).
 * - 2) evaluate the provided statement (using {@link org.junit.runners.model.Statement#evaluate})
 * - 3) disable log capture (to implement in child classes).
 */
public abstract class ILogStatement extends Statement implements ILogCapturer {
    private final Statement statement;

    public ILogStatement(final Statement statement) {
        this.statement = checkNotNull(statement, "Statement must NOT be null.");
    }

    @Override
    public void evaluate() throws Throwable {
        enableLogCapture();
        try {
            statement.evaluate();
        } finally {
            disableLogCapture();
        }
    }

    abstract public void enableLogCapture();

    abstract public void disableLogCapture();
}

package com.carmatechnologies.commons.testing.logging.junit;

import com.carmatechnologies.commons.testing.logging.api.ILogCapturer;
import org.junit.runners.model.Statement;

import java.util.Set;

import static com.carmatechnologies.commons.testing.utils.Preconditions.checkNotNull;

public class CompositeILogStatement extends ILogStatement implements ILogCapturer {
    private final Set<ILogCapturer> ILogCapturers;

    public CompositeILogStatement(final Statement statement, final Set<ILogCapturer> ILogCapturers) {
        super(statement);
        this.ILogCapturers = checkNotNull(ILogCapturers, "Log capturers must NOT be null set.");
    }

    @Override
    public void enableLogCapture() {
        for (final ILogCapturer ILogCapturer : ILogCapturers)
            ILogCapturer.enableLogCapture();
    }

    @Override
    public void disableLogCapture() {
        for (final ILogCapturer ILogCapturer : ILogCapturers)
            ILogCapturer.disableLogCapture();
    }
}

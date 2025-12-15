package org.example.tui;

import org.example.tui.input.InputEvent;
import org.example.tui.input.TerminalInput;
import org.jline.terminal.Size;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public final class EventLoop implements EventLoopHandle {
    private final Application app;
    private final TerminalInput input;
    private final AtomicReference<Size> pendingResize;
    private boolean redrawRequested = true;

    public EventLoop(Application app, TerminalInput input, AtomicReference<Size> pendingResize) {
        this.app = app;
        this.input = input;
        this.pendingResize = pendingResize;
    }

    @Override
    public void requestRedraw() {
        redrawRequested = true;
    }

    public void run() throws IOException {
        ControlFlow controlFlow = new ControlFlow.Wait();

        while (true) {
            Size size = pendingResize.getAndSet(null);
            if (size != null) {
                controlFlow = app.onEvent(new Event.Resize(size), this);
                if (controlFlow instanceof ControlFlow.Exit) {
                    break;
                }
            }

            long timeoutMillis = computeTimeoutMillis(controlFlow);
            InputEvent evt = input.read(timeoutMillis);

            if (evt == null) {
                controlFlow = app.onEvent(new Event.Tick(System.nanoTime()), this);
            } else {
                controlFlow = app.onEvent(new Event.Input(evt), this);
            }

            if (controlFlow instanceof ControlFlow.Exit) {
                break;
            }

            if (redrawRequested) {
                redrawRequested = false;
                app.onRedrawRequested();
            }
        }
    }

    private long computeTimeoutMillis(ControlFlow controlFlow) {
        if (controlFlow instanceof ControlFlow.Poll) {
            return 0L;
        }
        if (controlFlow instanceof ControlFlow.Exit) {
            return 0L;
        }
        if (controlFlow instanceof ControlFlow.Wait) {
            return 8L;
        }
        if (controlFlow instanceof ControlFlow.WaitUntil(long deadlineNanos)) {
            long now = System.nanoTime();
            long remaining = deadlineNanos - now;
            return remaining <= 0 ? 0L : remaining / 1_000_000L;
        }
        return 8L;
    }
}

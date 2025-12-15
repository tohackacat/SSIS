package org.example.tui;

import org.example.tui.input.InputEvent;
import org.jline.terminal.Size;

public sealed interface Event {
    enum RedrawRequested implements Event {Instance}

    record Input(InputEvent input) implements Event {
    }

    record Resize(Size size) implements Event {
    }

    record Tick(long nanos) implements Event {
    }
}

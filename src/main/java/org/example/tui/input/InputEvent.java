package org.example.tui.input;

public sealed interface InputEvent
        permits InputEvent.Key, InputEvent.Text, InputEvent.Paste, InputEvent.Mouse {
    record Key(KeyStroke stroke) implements InputEvent {
    }

    record Text(String text) implements InputEvent {
    }

    record Paste(String text) implements InputEvent {
    }

    record Mouse(MouseEvent event) implements InputEvent {
    }
}

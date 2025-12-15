package org.example.tui.input;

public record KeyStroke(Key key, Modifiers mods) {
    public static KeyStroke plain(Key key) {
        return new KeyStroke(key, Modifiers.none());
    }
}

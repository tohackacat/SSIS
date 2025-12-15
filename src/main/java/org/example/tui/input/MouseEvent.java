package org.example.tui.input;

public record MouseEvent(int x, int y, int buttons, boolean shift, boolean alt, boolean ctrl) {
}

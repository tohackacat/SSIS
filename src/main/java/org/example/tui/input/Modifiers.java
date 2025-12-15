package org.example.tui.input;

public record Modifiers(boolean ctrl, boolean alt, boolean shift, boolean meta) {
    public static Modifiers none() {
        return new Modifiers(false, false, false, false);
    }

    public Modifiers withCtrl() {
        return new Modifiers(true, alt, shift, meta);
    }

    public Modifiers withAlt() {
        return new Modifiers(ctrl, true, shift, meta);
    }

    public Modifiers withShift() {
        return new Modifiers(ctrl, alt, true, meta);
    }

    public Modifiers withMeta() {
        return new Modifiers(ctrl, alt, shift, true);
    }
}

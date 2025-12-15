package org.example.tui;

import org.example.tui.input.InputEvent;

public interface Layer {
    int zIndex();

    boolean blocksInput();

    boolean onInput(InputEvent event, UiContext ctx);

    void render(Frame frame, UiContext ctx);
}

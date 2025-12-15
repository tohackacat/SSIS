package org.example.tui.widget;

import org.example.tui.UiContext;
import org.example.tui.input.InputEvent;

public interface Widget {
    void render(Canvas canvas, UiContext ctx);

    boolean onInput(InputEvent event, UiContext ctx);

    boolean focusable();
}

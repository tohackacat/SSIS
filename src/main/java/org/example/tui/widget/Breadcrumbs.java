package org.example.tui.widget;

import org.example.tui.UiContext;
import org.jline.utils.AttributedStyle;

import java.util.List;

public final class Breadcrumbs implements Widget {
    private final List<String> parts;
    private final AttributedStyle style;

    public Breadcrumbs(List<String> parts, AttributedStyle style) {
        this.parts = parts;
        this.style = style;
    }

    @Override
    public void render(Canvas canvas, UiContext ctx) {
        Rect r = canvas.rect();
        if (r.w() <= 0 || r.h() <= 0) {
            return;
        }

        String s = String.join(" / ", parts);
        if (s.length() > r.w()) {
            int keep = Math.max(0, r.w() - 1);
            s = s.substring(Math.max(0, s.length() - keep));
            s = "…" + s;
        }
        canvas.putText(0, 0, s, style);
    }

    @Override
    public boolean onInput(org.example.tui.input.InputEvent event, UiContext ctx) {
        return false;
    }

    @Override
    public boolean focusable() {
        return false;
    }
}

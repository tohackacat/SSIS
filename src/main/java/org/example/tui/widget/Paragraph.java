package org.example.tui.widget;

import org.example.tui.UiContext;
import org.jline.utils.AttributedStyle;

public final class Paragraph implements Widget {
    private final String text;
    private final AttributedStyle style;

    public Paragraph(String text, AttributedStyle style) {
        this.text = text;
        this.style = style;
    }

    @Override
    public void render(Canvas canvas, UiContext ctx) {
        Rect r = canvas.rect();
        if (r.w() <= 0 || r.h() <= 0) {
            return;
        }

        var lines = TextLayout.wrap(text, style, r.w(), r.h());
        int maxRows = Math.min(r.h(), lines.length);
        for (int y = 0; y < maxRows; y++) {
            canvas.putRowCells(0, y, lines[y]);
        }
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

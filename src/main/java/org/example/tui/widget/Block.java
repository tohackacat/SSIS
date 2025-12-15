package org.example.tui.widget;

import org.example.tui.UiContext;
import org.example.tui.input.InputEvent;
import org.jline.utils.AttributedStyle;

public final class Block implements Widget {
    private final Widget child;
    private final String title;
    private final AttributedStyle style;
    private final AttributedStyle borderStyle;

    public Block(String title, Widget child, AttributedStyle style, AttributedStyle borderStyle) {
        this.title = title;
        this.child = child;
        this.style = style;
        this.borderStyle = borderStyle;
    }

    @Override
    public void render(Canvas canvas, UiContext ctx) {
        Rect r = canvas.rect();
        if (r.w() <= 0 || r.h() <= 0) {
            return;
        }

        canvas.fill(" ", style);

        if (r.w() >= 2 && r.h() >= 2) {
            canvas.putText(0, 0, "┌" + "─".repeat(Math.max(0, r.w() - 2)) + "┐", borderStyle);
            for (int y = 1; y < r.h() - 1; y++) {
                canvas.putText(0, y, "│", borderStyle);
                canvas.putText(r.w() - 1, y, "│", borderStyle);
            }
            canvas.putText(0, r.h() - 1, "└" + "─".repeat(Math.max(0, r.w() - 2)) + "┘", borderStyle);

            if (title != null && !title.isEmpty() && r.w() >= 4) {
                String t = " " + title + " ";
                int max = r.w() - 2;
                if (t.length() > max) {
                    t = t.substring(0, Math.max(0, max));
                }
                canvas.putText(2, 0, t, borderStyle);
            }
        }

        if (child != null && r.w() >= 3 && r.h() >= 3) {
            Rect inner = new Rect(r.x() + 1, r.y() + 1, r.w() - 2, r.h() - 2);
            child.render(new Canvas(ctx.frame(), inner), ctx);
        }
    }

    @Override
    public boolean onInput(InputEvent event, UiContext ctx) {
        if (child == null) {
            return false;
        }
        return child.onInput(event, ctx);
    }

    @Override
    public boolean focusable() {
        return child != null && child.focusable();
    }
}

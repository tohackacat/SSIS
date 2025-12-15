package org.example.tui.widget;

import org.example.tui.UiContext;
import org.example.tui.input.InputEvent;
import org.example.tui.input.Key;
import org.example.tui.input.KeyStroke;
import org.jline.utils.AttributedStyle;

import java.util.List;

public final class ListWidget implements Widget {
    private final String id;
    private final List<String> items;
    private final AttributedStyle normal;
    private final AttributedStyle selected;

    public ListWidget(String id, List<String> items, AttributedStyle normal, AttributedStyle selected) {
        this.id = id;
        this.items = items;
        this.normal = normal;
        this.selected = selected;
    }

    private static String padRight(String s, int w) {
        if (s.length() >= w) {
            return s;
        }
        return s + " ".repeat(w - s.length());
    }

    @Override
    public void render(Canvas canvas, UiContext ctx) {
        Rect r = canvas.rect();
        int sel = ctx.state().getInt(id + ".sel", 0);
        int scroll = ctx.state().getInt(id + ".scroll", 0);

        if (sel < 0) {
            sel = 0;
        }
        if (sel >= items.size()) {
            sel = Math.max(0, items.size() - 1);
        }

        if (scroll > sel) {
            scroll = sel;
        }
        if (scroll < sel - (r.h() - 1)) {
            scroll = Math.max(0, sel - (r.h() - 1));
        }

        ctx.state().putInt(id + ".sel", sel);
        ctx.state().putInt(id + ".scroll", scroll);

        for (int y = 0; y < r.h(); y++) {
            int idx = scroll + y;
            if (idx >= items.size()) {
                break;
            }
            String item = items.get(idx);
            if (item.length() > r.w()) {
                item = item.substring(0, Math.max(0, r.w() - 1)) + "…";
            }
            AttributedStyle st = (idx == sel) ? selected : normal;
            canvas.putText(0, y, padRight(item, r.w()), st);
        }
    }

    @Override
    public boolean onInput(InputEvent event, UiContext ctx) {
        if (event instanceof InputEvent.Key(KeyStroke stroke)) {
            if (stroke.key() instanceof Key.Named n) {
                if (n == Key.Named.Up) {
                    int sel = ctx.state().getInt(id + ".sel", 0);
                    ctx.state().putInt(id + ".sel", Math.max(0, sel - 1));
                    ctx.host().requestRedraw();
                    return true;
                }
                if (n == Key.Named.Down) {
                    int sel = ctx.state().getInt(id + ".sel", 0);
                    ctx.state().putInt(id + ".sel", Math.min(Math.max(0, items.size() - 1), sel + 1));
                    ctx.host().requestRedraw();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean focusable() {
        return true;
    }
}

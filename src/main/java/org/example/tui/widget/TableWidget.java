package org.example.tui.widget;

import org.example.tui.UiContext;
import org.example.tui.input.InputEvent;
import org.example.tui.input.Key;
import org.example.tui.input.KeyStroke;
import org.jline.utils.AttributedStyle;

import java.util.List;

public final class TableWidget implements Widget {
    private final String id;
    private final List<String> header;
    private final List<List<String>> rows;
    private final AttributedStyle headerStyle;
    private final AttributedStyle normal;
    private final AttributedStyle selected;

    public TableWidget(String id, List<String> header, List<List<String>> rows, AttributedStyle headerStyle, AttributedStyle normal, AttributedStyle selected) {
        this.id = id;
        this.header = header;
        this.rows = rows;
        this.headerStyle = headerStyle;
        this.normal = normal;
        this.selected = selected;
    }

    private static void renderRow(Canvas canvas, int y, List<String> cols, int colW, AttributedStyle style, int maxW) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cols.size(); i++) {
            String c = cols.get(i) == null ? "" : cols.get(i);
            if (c.length() > colW - 1) {
                c = c.substring(0, Math.max(0, colW - 2)) + "…";
            }
            sb.append(padRight(c, colW));
        }
        String line = sb.toString();
        if (line.length() > maxW) {
            line = line.substring(0, maxW);
        }
        canvas.putText(0, y, padRight(line, maxW), style);
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
        if (r.w() <= 0 || r.h() <= 0) {
            return;
        }

        int sel = ctx.state().getInt(id + ".sel", 0);
        int scroll = ctx.state().getInt(id + ".scroll", 0);

        if (sel < 0) sel = 0;
        if (sel >= rows.size()) sel = Math.max(0, rows.size() - 1);

        int bodyH = Math.max(0, r.h() - 1);
        if (scroll > sel) scroll = sel;
        if (scroll < sel - (bodyH - 1)) scroll = Math.max(0, sel - (bodyH - 1));

        ctx.state().putInt(id + ".sel", sel);
        ctx.state().putInt(id + ".scroll", scroll);

        int cols = header.size();
        int colW = cols == 0 ? r.w() : Math.max(1, r.w() / cols);

        renderRow(canvas, 0, header, colW, headerStyle, r.w());

        for (int y = 0; y < bodyH; y++) {
            int idx = scroll + y;
            if (idx >= rows.size()) {
                break;
            }
            AttributedStyle st = (idx == sel) ? selected : normal;
            renderRow(canvas, 1 + y, rows.get(idx), colW, st, r.w());
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
                    ctx.state().putInt(id + ".sel", Math.min(Math.max(0, rows.size() - 1), sel + 1));
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

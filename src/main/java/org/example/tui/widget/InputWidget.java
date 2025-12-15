package org.example.tui.widget;

import org.example.tui.Cell;
import org.example.tui.UiContext;
import org.example.tui.input.InputEvent;
import org.example.tui.input.Key;
import org.example.tui.input.KeyStroke;
import org.jline.utils.AttributedStyle;

public final class InputWidget implements Widget {
    private final String id;
    private final AttributedStyle style;
    private final AttributedStyle cursorStyle;

    public InputWidget(String id, AttributedStyle style, AttributedStyle cursorStyle) {
        this.id = id;
        this.style = style;
        this.cursorStyle = cursorStyle;
    }

    private static String normalize(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }
        String t = s.replace("\r", "");
        t = t.replace("\n", " ");
        return t;
    }

    private static int offsetByCodePoints(String s, int cpIndex) {
        return s.offsetByCodePoints(0, cpIndex);
    }

    private static int columnsBeforeCursor(String s, int cpCursor) {
        int off = s.offsetByCodePoints(0, cpCursor);
        String prefix = s.substring(0, off);
        return new org.example.tui.TerminalWidth().columnsOf(prefix);
    }

    @Override
    public void render(Canvas canvas, UiContext ctx) {
        Rect r = canvas.rect();
        String text = ctx.state().getString(id + ".text", "");
        int cursor = ctx.state().getInt(id + ".cursor", 0);

        int cpCount = text.codePointCount(0, text.length());
        if (cursor < 0) cursor = 0;
        if (cursor > cpCount) cursor = cpCount;
        ctx.state().putInt(id + ".cursor", cursor);

        Cell[] cells = Cell.fromString(text, style);
        int showLen = Math.min(r.w(), cells.length);
        if (showLen > 0) {
            Cell[] slice = new Cell[showLen];
            System.arraycopy(cells, 0, slice, 0, showLen);
            canvas.putRowCells(0, 0, slice);
        }
        if (cells.length < r.w()) {
            canvas.putText(cells.length, 0, " ".repeat(r.w() - cells.length), style);
        }

        int cursorCol = Math.min(r.w() - 1, Math.max(0, columnsBeforeCursor(text, cursor)));
        canvas.setCell(cursorCol, 0, Cell.of(" ", cursorStyle));
    }

    @Override
    public boolean onInput(InputEvent event, UiContext ctx) {
        if (event instanceof InputEvent.Text(String t)) {
            insert(ctx, t);
            return true;
        }
        if (event instanceof InputEvent.Paste(String t)) {
            insert(ctx, t);
            return true;
        }
        if (event instanceof InputEvent.Key(KeyStroke stroke)) {
            if (stroke.key() instanceof Key.Named n) {
                if (n == Key.Named.Backspace) {
                    backspace(ctx);
                    return true;
                }
                if (n == Key.Named.Left) {
                    move(ctx, -1);
                    return true;
                }
                if (n == Key.Named.Right) {
                    move(ctx, 1);
                    return true;
                }
                if (n == Key.Named.Delete) {
                    delete(ctx);
                    return true;
                }
            }
            if (stroke.key() instanceof Key.Char(int codePoint)) {
                String s = new String(Character.toChars(codePoint));
                insert(ctx, s);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean focusable() {
        return true;
    }

    private void insert(UiContext ctx, String chunk) {
        String add = normalize(chunk);
        if (add.isEmpty()) {
            return;
        }

        String text = ctx.state().getString(id + ".text", "");
        int cursor = ctx.state().getInt(id + ".cursor", 0);

        int off = offsetByCodePoints(text, cursor);
        String next = text.substring(0, off) + add + text.substring(off);

        int added = add.codePointCount(0, add.length());
        ctx.state().putString(id + ".text", next);
        ctx.state().putInt(id + ".cursor", cursor + added);
        ctx.host().requestRedraw();
    }

    private void backspace(UiContext ctx) {
        String text = ctx.state().getString(id + ".text", "");
        int cursor = ctx.state().getInt(id + ".cursor", 0);

        if (cursor <= 0) {
            return;
        }

        int off = offsetByCodePoints(text, cursor);
        int prevOff = offsetByCodePoints(text, cursor - 1);
        String next = text.substring(0, prevOff) + text.substring(off);

        ctx.state().putString(id + ".text", next);
        ctx.state().putInt(id + ".cursor", cursor - 1);
        ctx.host().requestRedraw();
    }

    private void delete(UiContext ctx) {
        String text = ctx.state().getString(id + ".text", "");
        int cursor = ctx.state().getInt(id + ".cursor", 0);

        int cpCount = text.codePointCount(0, text.length());
        if (cursor >= cpCount) {
            return;
        }

        int off = offsetByCodePoints(text, cursor);
        int nextOff = offsetByCodePoints(text, cursor + 1);
        String next = text.substring(0, off) + text.substring(nextOff);

        ctx.state().putString(id + ".text", next);
        ctx.host().requestRedraw();
    }

    private void move(UiContext ctx, int delta) {
        String text = ctx.state().getString(id + ".text", "");
        int cursor = ctx.state().getInt(id + ".cursor", 0);
        int cpCount = text.codePointCount(0, text.length());

        int next = cursor + delta;
        if (next < 0) next = 0;
        if (next > cpCount) next = cpCount;

        ctx.state().putInt(id + ".cursor", next);
        ctx.host().requestRedraw();
    }
}

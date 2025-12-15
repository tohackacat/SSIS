package org.example.tui.widget;

import org.example.tui.Cell;
import org.example.tui.Frame;
import org.jline.utils.AttributedStyle;

public final class Canvas {
    private final Frame frame;
    private final Rect rect;

    public Canvas(Frame frame, Rect rect) {
        this.frame = frame;
        this.rect = rect;
    }

    public Rect rect() {
        return rect;
    }

    public void fill(String glyph, AttributedStyle style) {
        String g = glyph == null || glyph.isEmpty() ? " " : glyph;
        Cell[] row = Cell.fromString(g.repeat(rect.w()), style);
        for (int y = 0; y < rect.h(); y++) {
            frame.putAt(rect.x(), rect.y() + y, row);
        }
    }

    public void putText(int x, int y, String text, AttributedStyle style) {
        putRowCells(x, y, Cell.fromString(text == null ? "" : text, style));
    }

    public void putRowCells(int x, int y, Cell[] cells) {
        if (cells == null || cells.length == 0) {
            return;
        }

        if (y < 0 || y >= rect.h()) {
            return;
        }

        if (x >= rect.w() || x + cells.length <= 0) {
            return;
        }

        int srcStart = Math.max(0, -x);
        int dstX = Math.max(0, x);
        int avail = rect.w() - dstX;
        int count = Math.min(avail, cells.length - srcStart);

        Cell[] slice = new Cell[count];
        System.arraycopy(cells, srcStart, slice, 0, count);

        frame.putAt(rect.x() + dstX, rect.y() + y, slice);
    }

    public void setCell(int x, int y, Cell cell) {
        if (x < 0 || x >= rect.w() || y < 0 || y >= rect.h()) {
            return;
        }
        int fx = rect.x() + x;
        int fy = rect.y() + y;
        if (fx < 0 || fx >= frame.width() || fy < 0 || fy >= frame.height()) {
            return;
        }
        frame.set(fx, fy, cell);
    }
}

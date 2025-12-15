package org.example.tui.widget;

import org.example.tui.Cell;
import org.jline.utils.AttributedStyle;

import java.util.ArrayList;
import java.util.List;

public final class TextLayout {
    public static Cell[][] wrap(String text, AttributedStyle style, int width, int maxLines) {
        if (width <= 0 || maxLines <= 0) {
            return new Cell[0][0];
        }

        String t = text == null ? "" : text;
        ArrayList<Cell[]> out = new ArrayList<>();

        int start = 0;
        while (true) {
            int nl = t.indexOf('\n', start);
            String part = nl < 0 ? t.substring(start) : t.substring(start, nl);
            appendWrapped(out, part, style, width, maxLines);

            if (out.size() >= maxLines) {
                break;
            }

            if (nl < 0) {
                break;
            }

            start = nl + 1;

            if (start == t.length()) {
                appendWrapped(out, "", style, width, maxLines);
                break;
            }
        }

        return out.toArray(new Cell[0][0]);
    }

    private static void appendWrapped(List<Cell[]> out, String part, AttributedStyle style, int width, int maxLines) {
        if (out.size() >= maxLines) {
            return;
        }

        Cell[] flat = Cell.fromString(part == null ? "" : part, style);
        if (flat.length == 0) {
            out.add(fillRow(style, width));
            return;
        }

        int i = 0;
        while (i < flat.length && out.size() < maxLines) {
            int n = Math.min(width, flat.length - i);
            Cell[] row = new Cell[width];
            System.arraycopy(flat, i, row, 0, n);
            for (int k = n; k < width; k++) {
                row[k] = Cell.emptyStyled(style);
            }
            out.add(row);
            i += n;
        }
    }

    private static Cell[] fillRow(AttributedStyle style, int width) {
        Cell[] row = new Cell[width];
        for (int i = 0; i < width; i++) {
            row[i] = Cell.emptyStyled(style);
        }
        return row;
    }
}

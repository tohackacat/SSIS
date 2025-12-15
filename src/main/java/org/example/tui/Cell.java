package org.example.tui;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public record Cell(String glyph, int width, AttributedStyle style, boolean continuation) {
    private static final TerminalWidth WIDTH = new TerminalWidth();

    public static Cell of(String glyph, AttributedStyle style) {
        String g = sanitizeGlyph(glyph);
        int w = Math.max(1, WIDTH.columnsOf(g));
        return new Cell(g, w, style, false);
    }

    public static Cell of(String glyph) {
        return of(glyph, AttributedStyle.DEFAULT);
    }

    public static Cell empty() {
        return new Cell(" ", 1, AttributedStyle.DEFAULT, false);
    }

    public static Cell emptyStyled(AttributedStyle style) {
        return new Cell(" ", 1, style, false);
    }

    public static Cell continuation(AttributedStyle style) {
        return new Cell("", 0, style, true);
    }

    public static Cell[] fromString(String string, AttributedStyle style) {
        String s = string == null ? "" : string;

        BreakIterator it = BreakIterator.getCharacterInstance(Locale.ROOT);
        it.setText(s);

        List<Cell> out = new ArrayList<>();
        int start = it.first();
        for (int end = it.next(); end != BreakIterator.DONE; start = end, end = it.next()) {
            String g = s.substring(start, end);
            Cell base = of(g, style);
            out.add(base);
            for (int k = 1; k < base.width(); k++) {
                out.add(continuation(style));
            }
        }

        return out.toArray(new Cell[0]);
    }

    public static Cell[] fromString(String string) {
        return fromString(string, AttributedStyle.DEFAULT);
    }

    public static Cell[] fromAttributedString(AttributedString attributedString) {
        String text = attributedString.toString();

        BreakIterator it = BreakIterator.getCharacterInstance(Locale.ROOT);
        it.setText(text);

        List<Cell> out = new ArrayList<>();
        int start = it.first();
        for (int end = it.next(); end != BreakIterator.DONE; start = end, end = it.next()) {
            AttributedStyle style = attributedString.styleAt(start);
            String g = text.substring(start, end);
            Cell base = of(g, style);
            out.add(base);
            for (int k = 1; k < base.width(); k++) {
                out.add(continuation(style));
            }
        }

        return out.toArray(new Cell[0]);
    }

    private static String sanitizeGlyph(String glyph) {
        if (glyph == null || glyph.isEmpty()) {
            return " ";
        }

        int i = 0;
        boolean changed = false;
        StringBuilder sb = null;

        while (i < glyph.length()) {
            int cp = glyph.codePointAt(i);
            boolean bad = cp == 0x1b || cp == '\n' || cp == '\r' || Character.isISOControl(cp);

            if (bad) {
                if (!changed) {
                    changed = true;
                    sb = new StringBuilder(glyph.length());
                    sb.append(glyph, 0, i);
                }
                sb.append(' ');
            } else if (changed) {
                sb.appendCodePoint(cp);
            }

            i += Character.charCount(cp);
        }

        if (!changed) {
            return glyph;
        }

        String s = sb.toString();
        return s.isEmpty() ? " " : s;
    }

    public String asString() {
        if (continuation) {
            return "";
        }
        return sanitizeGlyph(glyph);
    }
}

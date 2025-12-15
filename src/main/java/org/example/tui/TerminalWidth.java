package org.example.tui;

import org.jline.utils.WCWidth;

public final class TerminalWidth {
    public int columnsOf(String s) {
        int cols = 0;
        int i = 0;
        while (i < s.length()) {
            int cp = s.codePointAt(i);
            int w = WCWidth.wcwidth(cp);
            if (w > 0) {
                cols += w;
            }
            i += Character.charCount(cp);
        }
        return cols;
    }
}

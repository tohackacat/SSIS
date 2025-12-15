package org.example.tui.input;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.jline.utils.NonBlockingReader;

import java.io.IOException;

import static org.jline.keymap.KeyMap.key;

public final class TerminalInput {
    private final Terminal terminal;
    private final NonBlockingReader reader;
    private final BindingReader bindingReader;
    private final KeyMap<Token> keyMap;

    public TerminalInput(Terminal terminal) {
        this.terminal = terminal;
        this.reader = terminal.reader();
        this.bindingReader = new BindingReader(reader);
        this.keyMap = new KeyMap<>();
        installDefaultBindings();
    }

    public InputEvent read(long timeoutMillis) throws IOException {
        int peek = reader.peek(timeoutMillis);
        if (peek == NonBlockingReader.READ_EXPIRED) {
            return null;
        }
        if (peek < 0) {
            return null;
        }

        Token tok = bindingReader.readBinding(keyMap, null, true);
        if (tok == null) {
            return null;
        }

        if (tok instanceof Token.PasteStart) {
            return new InputEvent.Paste(readBracketedPasteBody());
        }

        if (tok instanceof Token.Special(KeyStroke stroke)) {
            return new InputEvent.Key(stroke);
        }

        String raw = bindingReader.getLastBinding();
        if (raw == null || raw.isEmpty()) {
            return null;
        }

        InputEvent maybeAlt = tryDecodeAltText(raw);
        if (maybeAlt != null) {
            return maybeAlt;
        }

        InputEvent maybeCtrl = tryDecodeCtrlChar(raw);
        if (maybeCtrl != null) {
            return maybeCtrl;
        }

        return decodePlainCharAsKeyOrText(raw);
    }

    public void enableBracketedPaste() {
        terminal.writer().write("\u001b[?2004h");
        terminal.flush();
    }

    public void disableBracketedPaste() {
        terminal.writer().write("\u001b[?2004l");
        terminal.flush();
    }

    private void installDefaultBindings() {
        keyMap.setAmbiguousTimeout(150L);

        keyMap.setUnicode(Token.Text.Instance);
        keyMap.setNomatch(Token.Text.Instance);

        keyMap.bind(Token.PasteStart.Instance, "\u001b[200~");
        keyMap.bind(Token.PasteEnd.Instance, "\u001b[201~");

        bindCap(InfoCmp.Capability.key_up, Key.Named.Up);
        bindCap(InfoCmp.Capability.key_down, Key.Named.Down);
        bindCap(InfoCmp.Capability.key_left, Key.Named.Left);
        bindCap(InfoCmp.Capability.key_right, Key.Named.Right);
        bindCap(InfoCmp.Capability.key_home, Key.Named.Home);
        bindCap(InfoCmp.Capability.key_end, Key.Named.End);
        bindCap(InfoCmp.Capability.key_ppage, Key.Named.PageUp);
        bindCap(InfoCmp.Capability.key_npage, Key.Named.PageDown);
        bindCap(InfoCmp.Capability.key_ic, Key.Named.Insert);
        bindCap(InfoCmp.Capability.key_dc, Key.Named.Delete);

        bindCap(InfoCmp.Capability.key_f1, Key.Named.F1);
        bindCap(InfoCmp.Capability.key_f2, Key.Named.F2);
        bindCap(InfoCmp.Capability.key_f3, Key.Named.F3);
        bindCap(InfoCmp.Capability.key_f4, Key.Named.F4);
        bindCap(InfoCmp.Capability.key_f5, Key.Named.F5);
        bindCap(InfoCmp.Capability.key_f6, Key.Named.F6);
        bindCap(InfoCmp.Capability.key_f7, Key.Named.F7);
        bindCap(InfoCmp.Capability.key_f8, Key.Named.F8);
        bindCap(InfoCmp.Capability.key_f9, Key.Named.F9);
        bindCap(InfoCmp.Capability.key_f10, Key.Named.F10);
        bindCap(InfoCmp.Capability.key_f11, Key.Named.F11);
        bindCap(InfoCmp.Capability.key_f12, Key.Named.F12);

        bindFallbackArrows();

        keyMap.bind(new Token.Special(KeyStroke.plain(Key.Named.Escape)), "\u001b");
        keyMap.bind(new Token.Special(KeyStroke.plain(Key.Named.Enter)), "\r");
        keyMap.bind(new Token.Special(KeyStroke.plain(Key.Named.Enter)), "\n");
        keyMap.bind(new Token.Special(KeyStroke.plain(Key.Named.Tab)), "\t");
        keyMap.bind(new Token.Special(KeyStroke.plain(Key.Named.Backspace)), "\u007f");
        keyMap.bind(new Token.Special(KeyStroke.plain(Key.Named.Backspace)), "\b");
    }

    private void bindFallbackArrows() {
        keyMap.bind(new Token.Special(KeyStroke.plain(Key.Named.Up)), "\u001b[A");
        keyMap.bind(new Token.Special(KeyStroke.plain(Key.Named.Down)), "\u001b[B");
        keyMap.bind(new Token.Special(KeyStroke.plain(Key.Named.Right)), "\u001b[C");
        keyMap.bind(new Token.Special(KeyStroke.plain(Key.Named.Left)), "\u001b[D");

        keyMap.bind(new Token.Special(KeyStroke.plain(Key.Named.Up)), "\u001bOA");
        keyMap.bind(new Token.Special(KeyStroke.plain(Key.Named.Down)), "\u001bOB");
        keyMap.bind(new Token.Special(KeyStroke.plain(Key.Named.Right)), "\u001bOC");
        keyMap.bind(new Token.Special(KeyStroke.plain(Key.Named.Left)), "\u001bOD");
    }

    private void bindCap(InfoCmp.Capability cap, Key.Named named) {
        String seq = key(terminal, cap);
        if (seq == null || seq.isEmpty()) {
            return;
        }
        keyMap.bind(new Token.Special(KeyStroke.plain(named)), seq);
    }

    private String readBracketedPasteBody() throws IOException {
        StringBuilder sb = new StringBuilder();
        StringBuilder window = new StringBuilder();

        while (true) {
            int ch = reader.read(0L);
            if (ch < 0) {
                break;
            }

            window.append((char) ch);

            if (window.length() > 8) {
                sb.append(window.charAt(0));
                window.deleteCharAt(0);
            }

            if (window.toString().endsWith("\u001b[201~")) {
                int keep = window.length() - "\u001b[201~".length();
                if (keep > 0) {
                    sb.append(window, 0, keep);
                }
                break;
            }
        }

        return sb.toString();
    }

    private InputEvent tryDecodeAltText(String raw) {
        if (raw.length() < 2) {
            return null;
        }
        if (raw.charAt(0) != 0x1b) {
            return null;
        }

        String rest = raw.substring(1);
        int cp = rest.codePointAt(0);
        int cpChars = Character.charCount(cp);
        if (cpChars != rest.length()) {
            return null;
        }

        KeyStroke stroke = new KeyStroke(new Key.Char(cp), Modifiers.none().withAlt());
        return new InputEvent.Key(stroke);
    }

    private InputEvent tryDecodeCtrlChar(String raw) {
        if (raw.length() != 1) {
            return null;
        }
        char c = raw.charAt(0);

        if (c >= 1 && c <= 26) {
            int letter = ('a' + (c - 1));
            KeyStroke stroke = new KeyStroke(new Key.Char(letter), Modifiers.none().withCtrl());
            return new InputEvent.Key(stroke);
        }

        return null;
    }

    private InputEvent decodePlainCharAsKeyOrText(String raw) {
        int cp = raw.codePointAt(0);
        int cpChars = Character.charCount(cp);

        if (cpChars != raw.length()) {
            return new InputEvent.Text(raw);
        }

        if (cp == 0x1b) {
            return new InputEvent.Text(raw);
        }

        if (cp < 0x20) {
            return new InputEvent.Text(raw);
        }

        return new InputEvent.Key(KeyStroke.plain(new Key.Char(cp)));
    }

    private sealed interface Token permits Token.Special, Token.Text, Token.PasteStart, Token.PasteEnd {
        enum Text implements Token {Instance}

        enum PasteStart implements Token {Instance}

        enum PasteEnd implements Token {Instance}

        record Special(KeyStroke stroke) implements Token {
        }
    }
}

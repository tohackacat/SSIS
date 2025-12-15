package org.example.ui;

import org.example.tui.Frame;
import org.example.tui.Layer;
import org.example.tui.UiContext;
import org.example.tui.input.InputEvent;
import org.example.tui.input.Key;
import org.example.tui.input.KeyStroke;
import org.example.tui.widget.*;
import org.jline.utils.AttributedStyle;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public final class UiOverlays {
    private UiOverlays() {
    }

    public static Layer message(String title, String message) {
        return new MessageLayer(title, message);
    }

    public static Layer confirm(String title, String question, BiConsumer<UiContext, Boolean> onDone) {
        return new ConfirmLayer(title, question, onDone);
    }

    public static Layer pickOne(String title, List<String> ids, List<String> labels, BiConsumer<UiContext, String> onPick) {
        return new PickerLayer(title, ids, labels, onPick);
    }

    public static Layer textInput(String title, String prompt, String initialValue, BiConsumer<UiContext, String> onDone) {
        return new TextInputLayer(title, prompt, initialValue, onDone);
    }

    private static Rect centered(Frame frame, int w, int h) {
        int ww = Math.min(frame.width(), Math.max(10, w));
        int hh = Math.min(frame.height(), Math.max(5, h));
        int x = (frame.width() - ww) / 2;
        int y = (frame.height() - hh) / 2;
        return new Rect(x, y, ww, hh);
    }

    private record MessageLayer(String title, String message) implements Layer {

        @Override
        public int zIndex() {
            return 100;
        }

        @Override
        public boolean blocksInput() {
            return true;
        }

        @Override
        public boolean onInput(InputEvent event, UiContext ctx) {
            if (event instanceof InputEvent.Key(KeyStroke stroke)) {
                if (stroke.key() instanceof Key.Named n) {
                    if (n == Key.Named.Enter || n == Key.Named.Escape) {
                        ctx.host().pop();
                        ctx.host().requestRedraw();
                        return true;
                    }
                }
            }
            return true;
        }

        @Override
        public void render(Frame frame, UiContext ctx) {
            AttributedStyle base = AttributedStyle.DEFAULT;
            Rect box = centered(frame, (int) (frame.width() * 0.7), (int) (frame.height() * 0.4));
            Canvas canvas = new Canvas(frame, box);
            Paragraph p = new Paragraph(message + "\n\nPress Enter or Esc.", base);
            new Block(title, p, base, base).render(canvas, ctx);
        }
    }

    private record ConfirmLayer(String title, String question, BiConsumer<UiContext, Boolean> onDone) implements Layer {

        @Override
        public int zIndex() {
            return 110;
        }

        @Override
        public boolean blocksInput() {
            return true;
        }

        @Override
        public boolean onInput(InputEvent event, UiContext ctx) {
            if (event instanceof InputEvent.Key(KeyStroke stroke)) {
                if (stroke.key() instanceof Key.Named n && n == Key.Named.Escape) {
                    ctx.host().pop();
                    onDone.accept(ctx, false);
                    ctx.host().requestRedraw();
                    return true;
                }
                if (stroke.key() instanceof Key.Char(int cp)) {
                    if (cp == 'y' || cp == 'Y') {
                        ctx.host().pop();
                        onDone.accept(ctx, true);
                        ctx.host().requestRedraw();
                        return true;
                    }
                    if (cp == 'n' || cp == 'N') {
                        ctx.host().pop();
                        onDone.accept(ctx, false);
                        ctx.host().requestRedraw();
                        return true;
                    }
                }
                if (stroke.key() instanceof Key.Named n2 && n2 == Key.Named.Enter) {
                    ctx.host().pop();
                    onDone.accept(ctx, true);
                    ctx.host().requestRedraw();
                    return true;
                }
            }
            return true;
        }

        @Override
        public void render(Frame frame, UiContext ctx) {
            AttributedStyle base = AttributedStyle.DEFAULT;
            Rect box = centered(frame, (int) (frame.width() * 0.7), (int) (frame.height() * 0.4));
            Canvas canvas = new Canvas(frame, box);
            Paragraph p = new Paragraph(question + "\n\nY = yes, N = no. Enter = yes. Esc = no.", base);
            new Block(title, p, base, base).render(canvas, ctx);
        }
    }

    private static final class PickerLayer implements Layer {
        private static final AtomicInteger SEQ = new AtomicInteger(0);

        private final String title;
        private final List<String> ids;
        private final ListWidget list;
        private final BiConsumer<UiContext, String> onPick;
        private final String listId;

        private PickerLayer(String title, List<String> ids, List<String> labels, BiConsumer<UiContext, String> onPick) {
            this.title = title;
            this.ids = ids;
            this.onPick = onPick;
            this.listId = "ui.picker." + SEQ.incrementAndGet();
            AttributedStyle base = AttributedStyle.DEFAULT;
            AttributedStyle sel = AttributedStyle.DEFAULT.inverse();
            this.list = new ListWidget(listId, labels, base, sel);
        }

        @Override
        public int zIndex() {
            return 120;
        }

        @Override
        public boolean blocksInput() {
            return true;
        }

        @Override
        public boolean onInput(InputEvent event, UiContext ctx) {
            if (event instanceof InputEvent.Key(KeyStroke stroke)) {
                if (stroke.key() instanceof Key.Named n) {
                    if (n == Key.Named.Escape) {
                        ctx.host().pop();
                        ctx.host().requestRedraw();
                        return true;
                    }
                    if (n == Key.Named.Enter) {
                        int sel = ctx.state().getInt(listId + ".sel", 0);
                        String picked = ids.isEmpty() ? "" : ids.get(Math.max(0, Math.min(ids.size() - 1, sel)));
                        ctx.host().pop();
                        onPick.accept(ctx, picked);
                        ctx.host().requestRedraw();
                        return true;
                    }
                }
            }
            list.onInput(event, ctx);
            return true;
        }

        @Override
        public void render(Frame frame, UiContext ctx) {
            AttributedStyle base = AttributedStyle.DEFAULT;
            Rect box = centered(frame, (int) (frame.width() * 0.8), (int) (frame.height() * 0.7));
            Canvas canvas = new Canvas(frame, box);
            new Block(title, list, base, base).render(canvas, ctx);
        }
    }

    private static final class TextInputLayer implements Layer {
        private static final AtomicInteger SEQ = new AtomicInteger(0);

        private final String title;
        private final String prompt;
        private final String initialValue;
        private final BiConsumer<UiContext, String> onDone;
        private final String inputId;
        private final org.example.tui.widget.InputWidget input;

        private TextInputLayer(String title, String prompt, String initialValue, BiConsumer<UiContext, String> onDone) {
            this.title = title;
            this.prompt = prompt;
            this.initialValue = initialValue == null ? "" : initialValue;
            this.onDone = onDone;
            this.inputId = "ui.text." + SEQ.incrementAndGet();
            AttributedStyle base = AttributedStyle.DEFAULT;
            AttributedStyle cursor = AttributedStyle.DEFAULT.inverse();
            this.input = new org.example.tui.widget.InputWidget(inputId, base, cursor);
        }

        @Override
        public int zIndex() {
            return 130;
        }

        @Override
        public boolean blocksInput() {
            return true;
        }

        @Override
        public boolean onInput(InputEvent event, UiContext ctx) {
            String initKey = inputId + ".init";
            if (!ctx.state().getBool(initKey, false)) {
                ctx.state().putString(inputId + ".text", initialValue);
                ctx.state().putInt(inputId + ".cursor", initialValue.codePointCount(0, initialValue.length()));
                ctx.state().putBool(initKey, true);
            }

            if (event instanceof InputEvent.Key(KeyStroke stroke)) {
                if (stroke.key() instanceof Key.Named n) {
                    if (n == Key.Named.Escape) {
                        ctx.host().pop();
                        ctx.host().requestRedraw();
                        return true;
                    }
                    if (n == Key.Named.Enter) {
                        String text = ctx.state().getString(inputId + ".text", "");
                        ctx.host().pop();
                        onDone.accept(ctx, text);
                        ctx.host().requestRedraw();
                        return true;
                    }
                }
            }
            return input.onInput(event, ctx);
        }

        @Override
        public void render(Frame frame, UiContext ctx) {
            AttributedStyle base = AttributedStyle.DEFAULT;
            Rect box = centered(frame, (int) (frame.width() * 0.8), 7);
            Canvas canvas = new Canvas(frame, box);

            int w = box.w();
            int h = box.h();

            Rect textArea = new Rect(box.x(), box.y(), w, h);
            Canvas outer = new Canvas(frame, textArea);

            Rect inner = new Rect(box.x() + 1, box.y() + 2, Math.max(0, w - 2), 1);
            Paragraph p = new Paragraph(prompt, base);

            p.render(new Canvas(frame, new Rect(box.x() + 1, box.y() + 1, Math.max(0, w - 2), 1)), ctx);
            input.render(new Canvas(frame, inner), ctx);
        }
    }
}

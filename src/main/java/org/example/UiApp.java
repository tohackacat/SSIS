package org.example;

import org.example.tui.*;
import org.example.tui.input.InputEvent;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.utils.Display;

public final class UiApp implements Application, UiHost {
    private final Terminal terminal;
    private final Display display;
    private final StateStore state = new StateStore();
    private final LayerStack stack = new LayerStack();
    private Frame frame;
    private EventLoopHandle handle;

    public UiApp(Terminal terminal, Screen root) {
        this.terminal = terminal;
        this.display = new Display(terminal, true);

        Size size = terminal.getSize();
        display.resize(size.getRows(), size.getColumns());
        this.frame = new Frame(size);

        stack.push(root);
    }

    private static boolean isCtrlC(InputEvent input) {
        if (input instanceof InputEvent.Key(org.example.tui.input.KeyStroke stroke)) {
            if (stroke.mods().ctrl() && stroke.key() instanceof org.example.tui.input.Key.Char(int cp)) {
                return cp == 'c' || cp == 'C';
            }
        }
        return false;
    }

    @Override
    public ControlFlow onEvent(Event event, EventLoopHandle handle) {
        this.handle = handle;

        if (event instanceof Event.Resize(Size size)) {
            display.resize(size.getRows(), size.getColumns());
            display.clear();
            display.reset();
            terminal.flush();
            frame = new Frame(size);
            requestRedraw();
            return new ControlFlow.Wait();
        }

        UiContext ctx = new UiContext(this, state, frame);

        if (event instanceof Event.Input(InputEvent input)) {
            if (isCtrlC(input)) {
                return new ControlFlow.Exit();
            }
            stack.onInput(input, ctx);
            requestRedraw();
            return stack.isEmpty() ? new ControlFlow.Exit() : new ControlFlow.Wait();
        }

        return stack.isEmpty() ? new ControlFlow.Exit() : new ControlFlow.Wait();
    }

    @Override
    public void onRedrawRequested() {
        frame.clear();
        UiContext ctx = new UiContext(this, state, frame);
        stack.render(frame, ctx);
        display.update(frame.prepare(), 0);
        terminal.flush();
    }

    @Override
    public void push(Layer layer) {
        stack.push(layer);
        requestRedraw();
    }

    @Override
    public void pop() {
        stack.pop();
        requestRedraw();
    }

    @Override
    public void requestRedraw() {
        if (handle != null) {
            handle.requestRedraw();
        }
    }
}

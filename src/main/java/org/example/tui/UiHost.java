package org.example.tui;

public interface UiHost {
    void push(Layer layer);

    void pop();

    void requestRedraw();
}

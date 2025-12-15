package org.example.tui;

public interface Application {
    ControlFlow onEvent(Event event, EventLoopHandle handle);

    void onRedrawRequested();
}

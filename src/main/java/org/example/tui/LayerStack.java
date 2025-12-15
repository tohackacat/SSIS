package org.example.tui;

import org.example.tui.input.InputEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class LayerStack {
    private final ArrayList<Layer> layers = new ArrayList<>();

    public void push(Layer layer) {
        layers.add(layer);
    }

    public void pop() {
        if (!layers.isEmpty()) {
            layers.removeLast();
        }
    }

    public boolean isEmpty() {
        return layers.isEmpty();
    }

    public boolean onInput(InputEvent event, UiContext ctx) {
        List<Layer> sorted = sortedByZ();
        for (int i = sorted.size() - 1; i >= 0; i--) {
            Layer layer = sorted.get(i);
            boolean handled = layer.onInput(event, ctx);
            if (handled) {
                return true;
            }
            if (layer.blocksInput()) {
                return true;
            }
        }
        return false;
    }

    public void render(Frame frame, UiContext ctx) {
        List<Layer> sorted = sortedByZ();
        for (Layer layer : sorted) {
            layer.render(frame, ctx);
        }
    }

    private List<Layer> sortedByZ() {
        ArrayList<Layer> out = new ArrayList<>(layers);
        out.sort(Comparator.comparingInt(Layer::zIndex));
        return out;
    }
}

package org.example.tui;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class StateStore {
    private final Map<String, Object> map = new HashMap<>();

    public <T> T getOrCreate(String key, Supplier<T> supplier, Class<T> cls) {
        Object v = map.get(key);
        if (v == null) {
            T created = supplier.get();
            map.put(key, created);
            return created;
        }
        return cls.cast(v);
    }

    public int getInt(String key, int def) {
        Object v = map.get(key);
        if (v instanceof Integer i) {
            return i;
        }
        return def;
    }

    public void putInt(String key, int value) {
        map.put(key, value);
    }

    public String getString(String key, String def) {
        Object v = map.get(key);
        if (v instanceof String s) {
            return s;
        }
        return def;
    }

    public void putString(String key, String value) {
        map.put(key, value);
    }

    public boolean getBool(String key, boolean def) {
        Object v = map.get(key);
        if (v instanceof Boolean b) {
            return b;
        }
        return def;
    }

    public void putBool(String key, boolean value) {
        map.put(key, value);
    }
}

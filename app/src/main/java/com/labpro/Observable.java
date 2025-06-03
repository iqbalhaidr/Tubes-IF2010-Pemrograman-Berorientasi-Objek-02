package com.labpro;

import java.util.ArrayList;
import java.util.List;

public class Observable {
    private ArrayList<Listener> listeners;

    public Observable() {
        listeners = new ArrayList<>();
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    public void notifyListeners(Object data, Listener.EventType eventType) {
        for (Listener listener : listeners) {
            listener.update(data, eventType);
        }
    }
}

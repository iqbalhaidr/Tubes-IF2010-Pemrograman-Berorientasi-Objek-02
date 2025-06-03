package com.labpro;

public interface Listener {
    enum EventType {
        Create,
        Update,
        Delete
    }
    public void update(Object data, EventType eventType);
}
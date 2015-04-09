package mixter.domain;

import mixter.Event;

public interface EventStore {
    void store(Event event);
}

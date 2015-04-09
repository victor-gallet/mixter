package mixter.infra;

import mixter.Event;
import mixter.EventPublisher;

import java.util.ArrayList;
import java.util.List;

public class SpyEventPublisher implements EventPublisher {
    public List<Event> publishedEvents = new ArrayList<>();

    public void publish(Event event) {
        publishedEvents.add(event);
    }
}


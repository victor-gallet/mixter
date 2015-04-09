package mixter;

import java.util.ArrayList;
import java.util.List;

public class SpyEventPublisher implements EventPublisher {
    public List<Event> publishedEvents = new ArrayList<>();

    public void publish(Event event) {
        publishedEvents.add(event);
    }
}

package mixter.infra;

import mixter.Event;
import mixter.EventPublisher;
import mixter.domain.EventStore;

public class PersistedEventPublisher implements EventPublisher{
    private EventStore store;
    private EventPublisher publisher;

    public PersistedEventPublisher(EventStore store, EventPublisher publisher) {
        super();
        this.store = store;
        this.publisher = publisher;
    }

    @Override
    public void publish(Event event) {
        store.store(event);
        publisher.publish(event);
    }
}

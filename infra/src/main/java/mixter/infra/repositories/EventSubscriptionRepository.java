package mixter.infra.repositories;

import mixter.Event;
import mixter.domain.EventStore;
import mixter.domain.core.subscription.Subscription;
import mixter.domain.core.subscription.SubscriptionId;
import mixter.domain.core.subscription.SubscriptionRepository;

import java.util.List;
import java.util.NoSuchElementException;

public class EventSubscriptionRepository implements SubscriptionRepository{
    private EventStore store;

    public EventSubscriptionRepository(EventStore store) {

        this.store = store;
    }

    @Override
    public Subscription getById(SubscriptionId subscriptionId) {
        List<Event> history = store.getEventsOfAggregate(subscriptionId);
        if(history.isEmpty()) {
            throw new NoSuchElementException();
        }else{
            return new Subscription(history);
        }
    }
}

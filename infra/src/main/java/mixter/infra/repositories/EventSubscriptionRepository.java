package mixter.infra.repositories;

import mixter.Event;
import mixter.domain.EventStore;
import mixter.domain.core.subscription.Subscription;
import mixter.domain.core.subscription.SubscriptionId;
import mixter.domain.core.subscription.SubscriptionRepository;

import java.util.List;

public class EventSubscriptionRepository extends AggregateRepository<Subscription> implements SubscriptionRepository {

    public EventSubscriptionRepository(EventStore store) {
        super(store);
    }

    @Override
    protected Subscription fromHistory(List<Event> history) {
        return new Subscription(history);
    }

    @Override
    public Subscription getById(SubscriptionId subscriptionId) {
        return super.getById(subscriptionId);
    }
}

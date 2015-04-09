package mixter;

import mixter.domain.core.message.Message;
import mixter.domain.core.subscription.Subscription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DomainTest {
    public List<Event> history(Event... events) {
        List<Event> eventHistory = new ArrayList<>();
        Collections.addAll(eventHistory, events);
        return eventHistory;
    }

    protected Subscription subscriptionFor(Event... events) {
        return new Subscription(history(events));
    }

    protected Message messageFor(Event... events) {
        return new Message(history(events));
    }
}

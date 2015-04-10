package mixter.domain;

import mixter.AggregateId;
import mixter.Event;

import java.util.List;

public interface EventStore {
    List<Event> getEventsOfAggregate(AggregateId aggregateId);

    void store(Event event);
}

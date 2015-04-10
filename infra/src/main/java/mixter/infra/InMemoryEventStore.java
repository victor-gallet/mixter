package mixter.infra;

import mixter.AggregateId;
import mixter.Event;
import mixter.domain.EventStore;

import java.util.*;

public class InMemoryEventStore implements EventStore {

    private Map<AggregateId,List<Event>> events=new HashMap<>();

    private List<Event> emptyList(){
        return new ArrayList<>();
    }

    @Override
    public List<Event> getEventsOfAggregate(AggregateId aggregateId) {
        return events.getOrDefault(aggregateId, emptyList());
    }

    @Override
    public void store(Event event) {
        List<Event> aggregateEvents = events.getOrDefault(event.getId(), emptyList());
        aggregateEvents.add(event);
        events.put(event.getId(), aggregateEvents);
    }
}

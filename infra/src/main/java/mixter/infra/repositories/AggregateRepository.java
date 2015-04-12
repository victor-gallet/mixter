package mixter.infra.repositories;

import mixter.AggregateId;
import mixter.Event;
import mixter.domain.EventStore;

import java.util.List;
import java.util.NoSuchElementException;

abstract public class AggregateRepository<T> {
    protected abstract T fromHistory(List<Event> history);
    private EventStore store;

    protected AggregateRepository(EventStore store) {
        this.store = store;
    }

    public T getById(AggregateId aggregateId) {
        List<Event> history = store.getEventsOfAggregate(aggregateId);
        if(history.isEmpty()) {
            throw new NoSuchElementException();
        }else{
            return fromHistory(history);
        }
    }
}

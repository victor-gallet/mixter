package mixter.infra.repositories;

import mixter.Event;
import mixter.domain.EventStore;
import mixter.domain.identity.Session;

import java.util.List;

public class EventSessionRepository extends AggregateRepository<Session> {

    public EventSessionRepository(EventStore store) {
        super(store);
    }

    @Override
    protected Session fromHistory(List<Event> history) {
        return new Session(history);
    }
}

package mixter.infra.repositories;

import mixter.Event;
import mixter.domain.EventStore;
import mixter.domain.identity.UserIdentity;

import java.util.List;

public class EventUserIdentityRepository extends AggregateRepository<UserIdentity> {

    public EventUserIdentityRepository(EventStore store) {
        super(store);
    }

    @Override
    protected UserIdentity fromHistory(List<Event> history) {
        return new UserIdentity(history);
    }
}

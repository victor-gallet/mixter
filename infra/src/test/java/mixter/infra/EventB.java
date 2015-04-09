package mixter.infra;

import mixter.AggregateId;
import mixter.Event;

class EventB implements Event{
    private AggregateId id;

    public EventB(AggregateId id) {
        this.id = id;
    }

    @Override
    public AggregateId getId() {
        return id;
    }
}

package mixter.infra;

import mixter.AggregateId;
import mixter.Event;

class EventA implements Event {
    private AggregateId id;
    private final int value;

    public EventA(AggregateId id) {
        this(id,0);
    }

    public EventA(AggregateId id, int i) {
        this.id = id;
        this.value = i;
    }

    @Override
    public AggregateId getId() {
        return id;
    }

    public int getValue() {
        return value;
    }
}

package mixter.infra;

import mixter.AggregateId;
import mixter.Event;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryEventStoreTest {
    public static final AnAggregateId AGGREGATE_ID1 = new AnAggregateId();
    public static final AnAggregateId AGGREGATE_ID2 = new AnAggregateId();

    InMemoryEventStore store = new InMemoryEventStore();
    @Before
    public void setUp() throws Exception {
        store=new InMemoryEventStore();

    }

    @Test
    public void WhenStoreEventOfAggregateThenCanGetThisEventOfAggregate() {
        // Given
        AggregateId aggregateId1 = AGGREGATE_ID1;

        // When
        store.store(new EventA(aggregateId1));

        // Then
        assertThat(store.getEventsOfAggregate(aggregateId1)).hasSize(1);
    }

    @Test
    public void GivenEventsOfSeveralAggregatesWhenGetEventsOfAggregateThenReturnEventsOfOnlyThisAggregate() {
        store.store(new EventA(AGGREGATE_ID1));
        store.store(new EventA(AGGREGATE_ID2));
        store.store(new EventA(AGGREGATE_ID1));

        List<Event> eventsOfAggregateA = store.getEventsOfAggregate(AGGREGATE_ID1);

        assertThat(eventsOfAggregateA).hasSize(2);
        assertThat(eventsOfAggregateA).extracting("id").containsExactly(AGGREGATE_ID1,AGGREGATE_ID1);
    }
    @Test
    public void GivenSeveralEventsWhenGetEventsOfAggregateThenReturnEventsAndPreserveOrder() {
        store.store(new EventA(AGGREGATE_ID1, 1));
        store.store(new EventA(AGGREGATE_ID1, 2));
        store.store(new EventA(AGGREGATE_ID1, 3));

        List<Event> eventsOfAggregateA = store.getEventsOfAggregate(AGGREGATE_ID1);

        assertThat(eventsOfAggregateA.toArray()).extracting("value").containsExactly(1, 2, 3);
    }

}

package mixter.infra;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PersistedEventPublisherTest {

    private InMemoryEventStore store;
    private SpyEventPublisher publisher;

    @Before
    public void setUp() throws Exception {
        store = new InMemoryEventStore();
        publisher = new SpyEventPublisher();
    }

    @Test
    public void WhenPublishEventThenStoreInDatabase() {
        // Given
        AnAggregateId id = new AnAggregateId();
        PersistedEventPublisher persistedPublisher = new PersistedEventPublisher(store, publisher);
        // When
        persistedPublisher.publish(new EventA(id));

        // Then
        assertThat(store.getEventsOfAggregate(id)).hasSize(1);
    }

    @Test
    public void WhenPublishEventThenCallEventHandlerBase() {
        // Given
        AnAggregateId id = new AnAggregateId();
        PersistedEventPublisher persistedPublisher = new PersistedEventPublisher(store, publisher);
        EventA event = new EventA(id);
        // When
        persistedPublisher.publish(event);

        // Then
        assertThat(publisher.publishedEvents).containsExactly(event);
    }
}

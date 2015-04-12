package mixter.infra.repositories;

import mixter.domain.EventStore;
import mixter.domain.identity.Session;
import mixter.domain.identity.SessionId;
import mixter.domain.identity.UserId;
import mixter.domain.identity.events.UserConnected;
import mixter.domain.identity.events.UserDisconnected;
import mixter.infra.InMemoryEventStore;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.Instant;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

public class EventSessionRepositoryTest {

    public static final SessionId SESSION_ID = SessionId.generate();
    public static final UserId USER_ID = new UserId("user@mix-it.fr");
    private EventStore eventStore;

    @Before
    public void setUp() throws Exception {
        eventStore = new InMemoryEventStore();
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldNotFindSessionIfNoEventForSessionId() throws Exception {
        //Given
        EventSessionRepository repository = new EventSessionRepository(eventStore);
        thrown.expect(NoSuchElementException.class);
        //When
        repository.getById(USER_ID);
    }

    @Test
    public void shouldFindSessionIfUserConnected() throws Exception {
        //Given
        EventSessionRepository repository = new EventSessionRepository(eventStore);
        eventStore.store(new UserConnected(SESSION_ID, USER_ID, Instant.now()));
        //When
        Session session = repository.getById(SESSION_ID);

        assertThat(session.getId()).isEqualTo(SESSION_ID);
    }

    @Test
    public void shouldNotFindSessionIfUserConnectedThenDisconnected() throws Exception {
        //Given
        EventSessionRepository repository = new EventSessionRepository(eventStore);
        eventStore.store(new UserConnected(SESSION_ID, USER_ID, Instant.now()));
        eventStore.store(new UserDisconnected(SESSION_ID, USER_ID));
        //When
        Session session = repository.getById(SESSION_ID);

        assertThat(session.getId()).isEqualTo(SESSION_ID);
    }
}

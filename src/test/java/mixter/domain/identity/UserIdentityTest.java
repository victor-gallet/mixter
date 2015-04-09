package mixter.domain.identity;

import mixter.DomainTest;
import mixter.Event;
import mixter.SpyEventPublisher;
import mixter.domain.identity.events.UserConnected;
import mixter.domain.identity.events.UserRegistered;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserIdentityTest extends DomainTest {

    public static final UserId USER_ID = new UserId("user@mix-it.fr");
    private SpyEventPublisher eventPublisher;

    @Before
    public void setUp() throws Exception {
        eventPublisher = new SpyEventPublisher();
    }

    @Test
    public void WhenRegisterThenRaiseUserRegisteredEvent() {
        // When
        UserIdentity.register(eventPublisher, USER_ID);

        // Then
        UserRegistered expected = new UserRegistered(USER_ID);
        assertThat(eventPublisher.publishedEvents).contains(expected);

    }

    @Test
    public void GivenUserRegisteredWhenLogThenRaiseUserConnectedEvent() {
        // Given
        UserIdentity userIdentity = userIdentityFor(new UserRegistered(USER_ID));
        // When
        userIdentity.logIn(eventPublisher);
        // Then
        UserConnected expected = new UserConnected(USER_ID);
        assertThat(eventPublisher.publishedEvents).contains(expected);
    }

    private UserIdentity userIdentityFor(Event... events) {
        return new UserIdentity(history(events));
    }
}

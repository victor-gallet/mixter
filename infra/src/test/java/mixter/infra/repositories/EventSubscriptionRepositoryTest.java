package mixter.infra.repositories;

import mixter.domain.EventStore;
import mixter.domain.core.message.MessageId;
import mixter.domain.core.subscription.Subscription;
import mixter.domain.core.subscription.SubscriptionId;
import mixter.domain.core.subscription.events.FolloweeMessagePublished;
import mixter.domain.core.subscription.events.UserFollowed;
import mixter.domain.core.subscription.events.UserUnfollowed;
import mixter.domain.identity.UserId;
import mixter.infra.InMemoryEventStore;
import mixter.infra.SpyEventPublisher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

public class EventSubscriptionRepositoryTest {

    public static final UserId FOLLOWER = new UserId("follower@mix-it.fr");
    public static final UserId FOLLOWEE = new UserId("followee@mix-it.fr");
    public static final MessageId MESSAGE_ID = new MessageId();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private EventStore store;
    private SpyEventPublisher publisher;

    @Before
    public void setUp() throws Exception {
        store = new InMemoryEventStore();
        publisher = new SpyEventPublisher();
    }

    @Test
    public void shouldFindNoSubscriptionWhenNoEvents() throws Exception {
        //Given
        EventSubscriptionRepository repository = new EventSubscriptionRepository(store);
        thrown.expect(NoSuchElementException.class);
        //When
        repository.getById(new SubscriptionId(FOLLOWER, FOLLOWEE));
    }

    @Test
    public void shouldFindActiveSubscriptionWhenUserFollowedInStore() throws Exception {
        //Given
        SubscriptionId subscriptionId = new SubscriptionId(FOLLOWER, FOLLOWEE);
        UserFollowed userFollowed = new UserFollowed(subscriptionId);

        store.store(userFollowed);
        EventSubscriptionRepository repository = new EventSubscriptionRepository(store);
        //When
        Subscription subscription = repository.getById(subscriptionId);
        subscription.notifyFollower(MESSAGE_ID, publisher);
        //Then
        FolloweeMessagePublished expected = new FolloweeMessagePublished(subscriptionId, MESSAGE_ID);
        assertThat(publisher.publishedEvents).containsExactly(expected);
    }

    @Test
    public void shouldFindInActiveSubscriptionWhenUserFollowedThenUnfollowedInStore() throws Exception {
        //Given
        SubscriptionId subscriptionId = new SubscriptionId(FOLLOWER, FOLLOWEE);
        UserFollowed userFollowed = new UserFollowed(subscriptionId);
        UserUnfollowed userUnfollowed = new UserUnfollowed(subscriptionId);

        store.store(userFollowed);
        store.store(userUnfollowed);
        EventSubscriptionRepository repository = new EventSubscriptionRepository(store);
        //When
        Subscription subscription = repository.getById(subscriptionId);
        subscription.notifyFollower(MESSAGE_ID, publisher);

        //Then
        assertThat(publisher.publishedEvents).isEmpty();
    }
}

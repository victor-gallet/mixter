package mixter.domain.core.subscription;

import mixter.DomainTest;
import mixter.SpyEventPublisher;
import mixter.domain.core.message.MessageId;
import mixter.domain.core.subscription.events.FolloweeMessagePublished;
import mixter.domain.core.subscription.events.UserFollowed;
import mixter.domain.core.subscription.events.UserUnfollowed;
import mixter.domain.identity.UserId;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SubscriptionTest extends DomainTest {

    private SpyEventPublisher eventPublisher;
    public static final UserId FOLLOWER = new UserId("follower@mix-it.fr");
    public static final UserId FOLLOWEE = new UserId("followee@mix-it.fr");
    public static final SubscriptionId SUBSCRIPTION_ID = new SubscriptionId(FOLLOWER, FOLLOWEE);

    @Before
    public void setUp() throws Exception {
        eventPublisher = new SpyEventPublisher();
    }

    @Test
    public void WhenAUserFollowsAnotherUserFollowedIsRaised() throws Exception {
        //Given

        //When
        Subscription.follow(FOLLOWER, FOLLOWEE, eventPublisher);

        //Then
        UserFollowed userFollowed = new UserFollowed(new SubscriptionId(FOLLOWER, FOLLOWEE));
        assertThat(eventPublisher.publishedEvents).containsExactly(userFollowed);
    }

    @Test
    public void WhenAUserUnfollowsAnotherUserUnfollowedIsRaised() throws Exception {
        //Given
        Subscription subscription = subscriptionFor(
                new UserFollowed(SUBSCRIPTION_ID)
        );
        //When
        subscription.unfollow(eventPublisher);

        //Then
        UserUnfollowed userUnfollowed = new UserUnfollowed(SUBSCRIPTION_ID);
        assertThat(eventPublisher.publishedEvents).containsExactly(userUnfollowed);
    }

    @Test
    public void WhenNotifyFollowerThenFollowerMessagePublishedIsRaised() {
        //Given
        Subscription subscription = subscriptionFor(
                new UserFollowed(SUBSCRIPTION_ID)
        );
        MessageId messageId = new MessageId();

        //When
        subscription.notifyFollower(messageId, eventPublisher);

        assertThat(eventPublisher.publishedEvents).containsExactly(new FolloweeMessagePublished(SUBSCRIPTION_ID, messageId));
    }

    @Test
    public void GivenUnfollowWhenNotifyFollowerThenDoNotRaisedFollowerMessagePublished() {
        // Given
        Subscription subscription = subscriptionFor(
                new UserFollowed(SUBSCRIPTION_ID),
                new UserUnfollowed(SUBSCRIPTION_ID)
        );
        MessageId messageId = new MessageId();

        // When
        subscription.notifyFollower(messageId, eventPublisher);

        assertThat(eventPublisher.publishedEvents).isEmpty();
    }
}

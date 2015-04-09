package mixter.domain.core.subscription.handlers;

import mixter.DomainTest;
import mixter.SpyEventPublisher;
import mixter.domain.core.message.MessageId;
import mixter.domain.core.message.events.MessagePublished;
import mixter.domain.core.message.events.MessageReplied;
import mixter.domain.core.message.events.MessageRepublished;
import mixter.domain.core.subscription.FakeFollowerRepository;
import mixter.domain.core.subscription.FakeSubscriptionRepository;
import mixter.domain.core.subscription.Subscription;
import mixter.domain.core.subscription.SubscriptionId;
import mixter.domain.core.subscription.events.FolloweeMessagePublished;
import mixter.domain.core.subscription.events.UserFollowed;
import mixter.domain.identity.UserId;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NotifyFollowerOfFolloweeMessageTest extends DomainTest {

    public static final String CONTENT = "Content";
    public static final UserId AUTHOR_ID = new UserId("author@mix-it.fr");
    public static final UserId FOLLOWER_ID = new UserId("follower@mix-it.fr");
    public static final UserId USER_ID = new UserId("someUser@mix-it.fr");
    public static final MessageId MESSAGE_ID = new MessageId();
    public static final MessageId REPLY_MESSAGE_ID = new MessageId();
    private SpyEventPublisher eventPublisher;
    private FakeSubscriptionRepository subscriptionRepository;
    private FakeFollowerRepository followerRepository;

    @Before
    public void setUp() throws Exception {
        eventPublisher = new SpyEventPublisher();
        subscriptionRepository = new FakeSubscriptionRepository();
        followerRepository = new FakeFollowerRepository();
    }

    @Test
    public void APublishedMessageShouldNotifyFollowers() {
        // Given
        MessagePublished messagePublished = new MessagePublished(MESSAGE_ID, CONTENT, AUTHOR_ID);
        subscriptionRepository.add(subscriptionFor(new UserFollowed(new SubscriptionId(FOLLOWER_ID, AUTHOR_ID))));
        followerRepository.add(AUTHOR_ID, FOLLOWER_ID);

        NotifyFollowerOfFolloweeMessage handler = new NotifyFollowerOfFolloweeMessage(followerRepository, subscriptionRepository, eventPublisher);

        // When
        handler.apply(messagePublished);
        // Then
        FolloweeMessagePublished followeeMessagePublished = new FolloweeMessagePublished(new SubscriptionId(FOLLOWER_ID, AUTHOR_ID), MESSAGE_ID);
        assertThat(eventPublisher.publishedEvents).containsExactly(followeeMessagePublished);
    }

    @Test
    public void ARepublishedMessageShouldNotifyFollowers() {
        // Given
        MessageRepublished messageRepublished = new MessageRepublished(MESSAGE_ID, USER_ID, AUTHOR_ID, CONTENT);
        Subscription subscription = subscriptionFor(
                new UserFollowed(new SubscriptionId(FOLLOWER_ID, AUTHOR_ID))
        );
        subscriptionRepository.add(subscription);
        followerRepository.add(AUTHOR_ID, FOLLOWER_ID);

        NotifyFollowerOfFolloweeMessage handler = new NotifyFollowerOfFolloweeMessage(followerRepository, subscriptionRepository, eventPublisher);

        // When
        handler.apply(messageRepublished);
        // Then
        FolloweeMessagePublished followeeMessagePublished = new FolloweeMessagePublished(new SubscriptionId(FOLLOWER_ID, AUTHOR_ID), MESSAGE_ID);
        assertThat(eventPublisher.publishedEvents).containsExactly(followeeMessagePublished);
    }

    @Test
    public void AReplyMessageShouldNotifyFollowers() {
        // Given
        MessageReplied messageReplied = new MessageReplied(AUTHOR_ID, USER_ID, CONTENT, MESSAGE_ID, REPLY_MESSAGE_ID);
        Subscription subscription = subscriptionFor(
                new UserFollowed(new SubscriptionId(FOLLOWER_ID, AUTHOR_ID))
        );
        subscriptionRepository.add(subscription);
        followerRepository.add(AUTHOR_ID, FOLLOWER_ID);

        NotifyFollowerOfFolloweeMessage handler = new NotifyFollowerOfFolloweeMessage(followerRepository, subscriptionRepository, eventPublisher);

        // When
        handler.apply(messageReplied);
        // Then
        FolloweeMessagePublished followeeMessagePublished = new FolloweeMessagePublished(new SubscriptionId(FOLLOWER_ID, AUTHOR_ID), REPLY_MESSAGE_ID);
        assertThat(eventPublisher.publishedEvents).containsExactly(followeeMessagePublished);
    }
}

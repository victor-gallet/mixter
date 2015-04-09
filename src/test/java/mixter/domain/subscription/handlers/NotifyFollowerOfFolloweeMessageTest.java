package mixter.domain.subscription.handlers;

import mixter.Event;
import mixter.SpyEventPublisher;
import mixter.UserId;
import mixter.domain.message.MessageId;
import mixter.domain.message.events.MessagePublished;
import mixter.domain.message.events.MessageReplied;
import mixter.domain.message.events.MessageRepublished;
import mixter.domain.subscription.FakeFollowerRepository;
import mixter.domain.subscription.FakeSubscriptionRepository;
import mixter.domain.subscription.Subscription;
import mixter.domain.subscription.SubscriptionId;
import mixter.domain.subscription.events.FolloweeMessagePublished;
import mixter.domain.subscription.events.UserFollowed;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class NotifyFollowerOfFolloweeMessageTest {

    public static final String CONTENT = "Content";
    public static final UserId AUTHOR_ID = new UserId();
    public static final UserId FOLLOWER_ID = new UserId();
    public static final UserId USER_ID = new UserId();
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

    Subscription subscriptionFor(Event... events) {
        return new Subscription(history(events));
    }

    public List<Event> history(Event... events) {
        List<Event> eventHistory = new ArrayList<>();
        Collections.addAll(eventHistory, events);
        return eventHistory;
    }
}

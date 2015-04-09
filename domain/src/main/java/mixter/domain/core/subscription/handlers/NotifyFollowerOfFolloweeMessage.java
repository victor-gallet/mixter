package mixter.domain.core.subscription.handlers;

import mixter.EventPublisher;
import mixter.domain.core.message.events.MessagePublished;
import mixter.domain.core.message.events.MessageReplied;
import mixter.domain.core.message.events.MessageRepublished;
import mixter.domain.core.subscription.FollowerRepository;
import mixter.domain.core.subscription.Subscription;
import mixter.domain.core.subscription.SubscriptionId;
import mixter.domain.core.subscription.SubscriptionRepository;
import mixter.domain.identity.UserId;

import java.util.Set;

public class NotifyFollowerOfFolloweeMessage {

    private FollowerRepository followerRepository;
    private SubscriptionRepository subscriptionRepository;
    private EventPublisher eventPublisher;

    public NotifyFollowerOfFolloweeMessage(FollowerRepository followerRepository, SubscriptionRepository subscriptionRepository, EventPublisher eventPublisher) {
        this.followerRepository = followerRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.eventPublisher = eventPublisher;
    }

    public void apply(MessagePublished messagePublished) {
        Set<UserId> followers = followerRepository.getFollowers(messagePublished.getAuthorId());
        for (UserId follower : followers) {
            Subscription subscription = subscriptionRepository.getById(new SubscriptionId(follower, messagePublished.getAuthorId()));
            subscription.notifyFollower(messagePublished.getMessageId(), eventPublisher);
        }
    }

    public void apply(MessageRepublished messageRepublished) {
        Set<UserId> followers = followerRepository.getFollowers(messageRepublished.getAuthorId());
        for (UserId follower : followers) {
            Subscription subscription = subscriptionRepository.getById(new SubscriptionId(follower, messageRepublished.getAuthorId()));
            subscription.notifyFollower(messageRepublished.getMessageId(), eventPublisher);
        }
    }

    public void apply(MessageReplied messageReplied) {
        Set<UserId> followers = followerRepository.getFollowers(messageReplied.getAuthorId());
        for (UserId follower : followers) {
            Subscription subscription = subscriptionRepository.getById(new SubscriptionId(follower, messageReplied.getAuthorId()));
            subscription.notifyFollower(messageReplied.getMessageId(), eventPublisher);
        }
    }
}

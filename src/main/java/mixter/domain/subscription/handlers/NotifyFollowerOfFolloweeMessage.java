package mixter.domain.subscription.handlers;

import mixter.EventPublisher;
import mixter.UserId;
import mixter.domain.message.events.MessagePublished;
import mixter.domain.message.events.MessageReplied;
import mixter.domain.message.events.MessageRepublished;
import mixter.domain.subscription.FollowerRepository;
import mixter.domain.subscription.Subscription;
import mixter.domain.subscription.SubscriptionId;
import mixter.domain.subscription.SubscriptionRepository;

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

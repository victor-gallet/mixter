package mixter.domain.core.subscription;

import mixter.Event;
import mixter.EventPublisher;
import mixter.doc.Aggregate;
import mixter.doc.Projection;
import mixter.domain.core.message.MessageId;
import mixter.domain.core.subscription.events.FolloweeMessagePublished;
import mixter.domain.core.subscription.events.UserFollowed;
import mixter.domain.core.subscription.events.UserUnfollowed;
import mixter.domain.identity.UserId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Aggregate
public class Subscription {
    private final DecisionProjection projection;

    public Subscription(List<Event> events) {
        projection = new DecisionProjection(events);
    }

    public static void follow(UserId follower, UserId followee, EventPublisher eventPublisher) {
        UserFollowed userFollowed = new UserFollowed(new SubscriptionId(follower, followee));
        eventPublisher.publish(userFollowed);
    }

    public void unfollow(EventPublisher eventPublisher) {
        eventPublisher.publish(new UserUnfollowed(projection.getId()));
    }

    public void notifyFollower(MessageId messageId, EventPublisher eventPublisher) {
        if (projection.isActive()) {
            eventPublisher.publish(new FolloweeMessagePublished(projection.getId(), messageId));
        }
    }

    public SubscriptionId getId() {
        return projection.getId();
    }

    @Projection
    class DecisionProjection {
        public SubscriptionId id;
        public boolean active;
        private Map<Class, Consumer> appliers = new HashMap<>();

        public DecisionProjection(List<Event> eventHistory) {
            Consumer<UserFollowed> applyUserFollowed = this::apply;
            Consumer<UserUnfollowed> applyUserUnfollowed = this::apply;
            appliers.put(UserFollowed.class, applyUserFollowed);
            appliers.put(UserUnfollowed.class, applyUserUnfollowed);
            eventHistory.forEach(this::apply);
        }

        @SuppressWarnings("unchecked")
        public void apply(Event event) {
            Consumer consumer = appliers.get(event.getClass());
            consumer.accept(event);
        }

        private void apply(UserFollowed userFollowed) {
            id = userFollowed.getSubscriptionId();
            active = true;
        }

        private void apply(UserUnfollowed userUnfollowed) {
            active = false;
        }

        public boolean isActive() {
            return active;
        }

        private SubscriptionId getId() {
            return id;
        }
    }
}

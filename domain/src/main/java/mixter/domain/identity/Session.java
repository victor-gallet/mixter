package mixter.domain.identity;

import mixter.Event;
import mixter.EventPublisher;
import mixter.doc.Aggregate;
import mixter.doc.Projection;
import mixter.domain.identity.events.UserConnected;
import mixter.domain.identity.events.UserDisconnected;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Aggregate
public class Session {
    private DecisionProjection projection;

    public Session(List<Event> history) {
        projection = new DecisionProjection(history);
    }


    public void logout(EventPublisher eventPublisher) {
        if (projection.active) {
            eventPublisher.publish(new UserDisconnected(projection.getId(), projection.getUserId()));
        }
    }

    public SessionId getId() {
        return projection.getId();
    }

    @Projection
    private class DecisionProjection {
        private SessionId id;
        private UserId userId;
        private Map<Class, Consumer> appliers = new HashMap<>();
        private boolean active = true;

        public DecisionProjection(List<Event> history) {
            Consumer<UserConnected> applyUserConnected = this::apply;
            Consumer<UserDisconnected> applyUserDisconnected = this::apply;
            appliers.put(UserConnected.class, applyUserConnected);
            appliers.put(UserDisconnected.class, applyUserDisconnected);
            history.forEach(this::apply);
        }

        public void apply(UserDisconnected event) {
            active = false;
        }

        public void apply(UserConnected event) {
            id = event.getSessionId();
            userId = event.getUserId();
        }

        @SuppressWarnings("unchecked")
        public void apply(Event event) {
            Consumer consumer = appliers.get(event.getClass());
            consumer.accept(event);
        }

        public SessionId getId() {
            return id;
        }

        public UserId getUserId() {
            return userId;
        }
    }
}

package mixter.domain.identity.events;

import mixter.AggregateId;
import mixter.Event;
import mixter.domain.identity.SessionId;
import mixter.domain.identity.UserId;

import java.time.Instant;

public class UserConnected implements Event {
    private final SessionId sessionId;
    private UserId userId;
    private final Instant now;

    public UserConnected(SessionId sessionId, UserId userId, Instant now) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.now = now;
    }

    public UserId getUserId() {
        return userId;
    }

    public SessionId getSessionId() {
        return sessionId;
    }

    public Instant getNow() {
        return now;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserConnected that = (UserConnected) o;

        if (sessionId != null ? !sessionId.equals(that.sessionId) : that.sessionId != null)
            return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null)
            return false;
        return !(now != null ? !now.equals(that.now) : that.now != null);

    }

    @Override
    public int hashCode() {
        int result = sessionId != null ? sessionId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (now != null ? now.hashCode() : 0);
        return result;
    }

    @Override
    public AggregateId getId() {
        return sessionId;
    }
}

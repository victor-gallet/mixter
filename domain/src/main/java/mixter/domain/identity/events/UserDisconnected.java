package mixter.domain.identity.events;

import mixter.Event;
import mixter.domain.identity.SessionId;
import mixter.domain.identity.UserId;

public class UserDisconnected implements Event {
    private SessionId sessionId;
    private UserId userId;

    public UserDisconnected(SessionId sessionId, UserId userId) {
        this.sessionId = sessionId;
        this.userId = userId;
    }

    public UserId getUserId() {
        return userId;
    }

    public SessionId getSessionId() {
        return sessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDisconnected that = (UserDisconnected) o;

        if (sessionId != null ? !sessionId.equals(that.sessionId) : that.sessionId != null)
            return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sessionId != null ? sessionId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}

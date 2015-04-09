package mixter.domain.identity;

public class SessionProjection {
    private final SessionId sessionId;
    private final UserId userId;
    private final boolean active;

    public SessionProjection(SessionId sessionId, UserId userId, boolean active) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.active = active;
    }

    public SessionId getSessionId() {
        return sessionId;
    }

    public UserId getUserId() {
        return userId;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionProjection that = (SessionProjection) o;

        if (active != that.active) return false;
        if (sessionId != null ? !sessionId.equals(that.sessionId) : that.sessionId != null)
            return false;
        return !(userId != null ? !userId.equals(that.userId) : that.userId != null);

    }

    @Override
    public int hashCode() {
        int result = sessionId != null ? sessionId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        return result;
    }
}

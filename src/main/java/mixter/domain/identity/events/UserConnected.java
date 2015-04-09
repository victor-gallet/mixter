package mixter.domain.identity.events;

import mixter.Event;
import mixter.domain.identity.UserId;

public class UserConnected implements Event {
    private UserId userId;

    public UserConnected(UserId userId) {
        this.userId = userId;
    }

    public UserId getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserConnected that = (UserConnected) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }
}

package mixter.domain.identity;

import mixter.AggregateId;

public class UserId implements AggregateId{
    private String email;

    public UserId(String email) {
        if (email.trim().isEmpty()) {
            throw new UserEmailCannotBeEmpty();
        }
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserId userId = (UserId) o;

        if (email != null ? !email.equals(userId.email) : userId.email != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }

    @Override
    public String toString() {
        return email;
    }
}

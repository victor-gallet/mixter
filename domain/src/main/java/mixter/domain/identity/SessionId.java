package mixter.domain.identity;

import mixter.AggregateId;

import java.util.UUID;

public class SessionId implements AggregateId{
    private String value;

    private SessionId(String value) {
        this.value = value;
    }

    public static SessionId generate() {
        return new SessionId(UUID.randomUUID().toString());
    }
}

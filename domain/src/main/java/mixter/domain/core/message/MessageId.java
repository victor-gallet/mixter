package mixter.domain.core.message;

import mixter.AggregateId;

import java.util.UUID;

public class MessageId implements AggregateId{
    private final String id;

    public MessageId() {
        this.id = UUID.randomUUID().toString();
    }
    public MessageId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageId messageId = (MessageId) o;

        return id.equals(messageId.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id;
    }

    public String getId() {
        return id;
    }
}

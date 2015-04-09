package mixter.domain.core.message;

public interface TimelineRepository {
    TimelineMessage save(TimelineMessage message);

    TimelineMessage getByMessageId(MessageId messageId);
}

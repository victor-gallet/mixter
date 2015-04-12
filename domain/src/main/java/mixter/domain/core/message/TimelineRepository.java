package mixter.domain.core.message;

import mixter.doc.Repository;

@Repository
public interface TimelineRepository {
    TimelineMessage save(TimelineMessage message);

    TimelineMessage getByMessageId(MessageId messageId);

    void removeByMessageId(MessageId messageId);
}

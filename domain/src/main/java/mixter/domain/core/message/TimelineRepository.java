package mixter.domain.core.message;

import mixter.doc.Repository;
import mixter.domain.identity.UserId;

import java.util.List;

@Repository
public interface TimelineRepository {
    TimelineMessage save(TimelineMessage message);

    TimelineMessage getByMessageId(MessageId messageId);

    void removeByMessageId(MessageId messageId);

    List<TimelineMessage> getByUserId(UserId userId);
}

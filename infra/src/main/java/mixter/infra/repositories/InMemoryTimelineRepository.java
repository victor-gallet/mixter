package mixter.infra.repositories;

import mixter.domain.core.message.MessageId;
import mixter.domain.core.message.TimelineMessage;
import mixter.domain.core.message.TimelineRepository;
import mixter.domain.identity.UserId;

import java.util.*;

public class InMemoryTimelineRepository implements TimelineRepository {
    private Set<TimelineMessage> messages=new LinkedHashSet<>();

    @Override
    public TimelineMessage save(TimelineMessage message) {
        messages.add(message);
        return message;
    }

    @Override
    public TimelineMessage getByMessageId(MessageId messageId) {
        Optional<TimelineMessage> first = messages.stream()
                .filter(timelineMessage -> timelineMessage.getMessageId().equals(messageId))
                .findFirst();
       return first.get();
    }

    @Override
    public void removeByMessageId(MessageId messageId) {
        messages.removeIf(message->message.getMessageId().equals(messageId));
    }

    @Override
    public List<TimelineMessage> getByUserId(UserId userId) {
        ArrayList<TimelineMessage> timelineMessages = new ArrayList<>(messages);
        timelineMessages.removeIf(x -> !x.getOwnerId().equals(userId));
        return timelineMessages;
    }
}

package mixter.infra.repositories;

import mixter.domain.core.message.MessageId;
import mixter.domain.core.message.TimelineMessage;
import mixter.domain.core.message.TimelineRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class InMemoryTimelineRepository implements TimelineRepository {
    private Set<TimelineMessage> messages=new HashSet<>();

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
}

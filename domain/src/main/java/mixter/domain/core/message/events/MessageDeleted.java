package mixter.domain.core.message.events;

import mixter.Event;
import mixter.domain.core.message.MessageId;

public class MessageDeleted implements Event {
    private MessageId messageId;

    public MessageDeleted(MessageId messageId) {
        this.messageId = messageId;
    }

    public MessageId getMessageId() {
        return messageId;
    }
}

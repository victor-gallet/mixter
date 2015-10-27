package mixter.domain.core.message.handlers;

import mixter.doc.Handler;
import mixter.domain.core.message.TimelineMessageProjection;
import mixter.domain.core.message.TimelineMessageRepository;
import mixter.domain.core.message.events.MessageQuacked;

@Handler
public class UpdateTimeline {
    private TimelineMessageRepository repository;

	public UpdateTimeline(TimelineMessageRepository repository) {
		this.repository = repository;
    }

	public void apply(MessageQuacked messageQuacked) {
		repository.save(new TimelineMessageProjection(messageQuacked.getAuthorId(), messageQuacked.getAuthorId(), messageQuacked.getMessage(), messageQuacked.getMessageId()));
	}
}

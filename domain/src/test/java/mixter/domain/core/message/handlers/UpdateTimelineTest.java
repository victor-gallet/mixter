package mixter.domain.core.message.handlers;

import mixter.domain.core.message.MessageId;
import mixter.domain.core.message.TimelineMessage;
import mixter.domain.core.message.TimelineRepository;
import mixter.domain.core.message.events.MessageDeleted;
import mixter.domain.core.message.events.MessagePublished;
import mixter.domain.core.message.events.MessageRepublished;
import mixter.domain.core.subscription.SubscriptionId;
import mixter.domain.core.subscription.events.FolloweeMessagePublished;
import mixter.domain.identity.UserId;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateTimelineTest {

    public static final UserId AUTHOR_ID = new UserId("author@mix-it.fr");
    public static final UserId USER_ID = new UserId("someUser@mix-it.fr");
    public static final String CONTENT = "hello world";
    private TimelineRepositoryFake timelineRepository;

    @Before
    public void setUp() throws Exception {
        timelineRepository = new TimelineRepositoryFake();
    }

    @Test
    public void WhenUpdateTimelineAppliesAMessagePublishedEventThenATimelineMessageIsCreated() {
        // Given
        MessageId messageId = new MessageId();
        MessagePublished messagePublished = new MessagePublished(messageId, CONTENT, AUTHOR_ID);
        UpdateTimeline handler = new UpdateTimeline(timelineRepository);
        // When
        handler.apply(messagePublished);
        // Then
        assertThat(timelineRepository.getMessages()).containsExactly(new TimelineMessage(AUTHOR_ID, AUTHOR_ID, CONTENT, messageId));
    }

    @Test
    public void WhenUpdateTimelineAppliesAMessageRepublishedEventThenATimelineMessageIsCreated() {
        // Given
        MessageId messageId = new MessageId();
        MessageRepublished messageRepublished = new MessageRepublished(messageId, USER_ID, AUTHOR_ID, CONTENT);
        UpdateTimeline handler = new UpdateTimeline(timelineRepository);
        // When
        handler.apply(messageRepublished);
        // Then
        assertThat(timelineRepository.getMessages()).containsExactly(new TimelineMessage(USER_ID, AUTHOR_ID, CONTENT, messageId));
    }

    @Test
    public void WhenUpdateTimelineAppliesAMessageDeletedEventThenTheTimelineMessagesWithThisMessageIdAreDeleted() {
        // Given
        MessageId messageId = new MessageId();
        MessagePublished messagePublished = new MessagePublished(messageId, CONTENT, AUTHOR_ID);
        MessageRepublished messageRepublished = new MessageRepublished(messageId, USER_ID, AUTHOR_ID, CONTENT);
        UpdateTimeline handler = new UpdateTimeline(timelineRepository);
        handler.apply(messagePublished);
        handler.apply(messageRepublished);

        MessageDeleted messageDeleted= new MessageDeleted(messageId);

        // When
        handler.apply(messageDeleted);

        // Then
        assertThat(timelineRepository.getMessages()).isEmpty();
    }

    @Test
    public void WhenUpdateTimelineAppliesAMessageDeletedEventThenTheTimelineMessagesWithOtherMessageIdAreNotDeleted() {
        // Given
        MessageId messageId = new MessageId();
        MessageId messageId2 = new MessageId();
        MessagePublished messagePublished = new MessagePublished(messageId, CONTENT, AUTHOR_ID);
        MessageRepublished messageRepublished = new MessageRepublished(messageId2, USER_ID, AUTHOR_ID, CONTENT);
        UpdateTimeline handler = new UpdateTimeline(timelineRepository);
        handler.apply(messagePublished);
        handler.apply(messageRepublished);

        MessageDeleted messageDeleted= new MessageDeleted(messageId);

        // When
        handler.apply(messageDeleted);

        // Then
        assertThat(timelineRepository.getMessages()).containsExactly(new TimelineMessage(USER_ID, AUTHOR_ID, CONTENT, messageId2));
    }
    @Test
    public void WhenUpdateTimelineAppliesAFolloweeMessagePublishedEventThenATimelineMessageIsCreated() {
        // Given
        MessageId messageId = new MessageId();
        FolloweeMessagePublished messagePublished = new FolloweeMessagePublished(new SubscriptionId(USER_ID, AUTHOR_ID), messageId);
        UpdateTimeline handler = new UpdateTimeline(timelineRepository);
        timelineRepository.save(new TimelineMessage(AUTHOR_ID, AUTHOR_ID, CONTENT, messageId));
        // When
        handler.apply(messagePublished);
        // Then
        assertThat(timelineRepository.getMessages()).containsExactly(new TimelineMessage(USER_ID, AUTHOR_ID, CONTENT, messageId));
    }

    class TimelineRepositoryFake implements TimelineRepository {
        List<TimelineMessage> messages = new ArrayList<>();

        public List<TimelineMessage> getMessages() {
            return messages;
        }

        @Override
        public TimelineMessage save(TimelineMessage message) {
            messages.removeIf(timelineMessage -> timelineMessage.getMessageId().equals(message.getMessageId()));
            messages.add(message);
            return message;
        }

        @Override
        public TimelineMessage getByMessageId(MessageId messageId) {
            return messages.stream().filter(message -> message.getMessageId() == messageId).findFirst().get();
        }

        @Override
        public void removeByMessageId(MessageId messageId) {
            messages.removeIf(timelineMessage -> timelineMessage.getMessageId().equals(messageId));
        }

        @Override
        public List<TimelineMessage> getByUserId(UserId userId) {
            return Lists.emptyList();
        }
    }
}

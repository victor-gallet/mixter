package mixter.domain.core.message;

import mixter.DomainTest;
import mixter.Event;
import mixter.SpyEventPublisher;
import mixter.domain.core.message.events.MessageDeleted;
import mixter.domain.core.message.events.MessagePublished;
import mixter.domain.core.message.events.MessageReplied;
import mixter.domain.core.message.events.MessageRepublished;
import mixter.domain.identity.UserId;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageTest extends DomainTest {

    public static final UserId AUTHOR_ID = new UserId("author@mix-it.fr");
    public static final String CONTENT = "hello";
    public static final UserId USER_ID = new UserId("someUser@mix-it.fr");
    public static final UserId REPLIER_ID = new UserId("replier@mix-it.fr");
    public static final String REPLY_CONTENT = "reply content";
    public static final UserId RANDOM_GUY = new UserId("randomeGuy@mix-it.fr");
    private SpyEventPublisher eventPublisher;

    @Before
    public void initialize() {
        eventPublisher = new SpyEventPublisher();
    }

    @Test
    public void whenAMessageIsCreatedByAPublishMessageCommandThenItSendsAMessagePublishedEvent() {
        // Given
        PublishMessage publishMessage = new PublishMessage(CONTENT, AUTHOR_ID);

        // When
        Message.publish(publishMessage, eventPublisher);

        // Then
        MessagePublished expectedEvent = new MessagePublished(new MessageId(), CONTENT, AUTHOR_ID);
        assertThat(eventPublisher.publishedEvents).extracting("message").containsExactly(expectedEvent.getMessage());
    }

    @Test
    public void whenAMessageIsRepublishedThenItSendsAMessageRepublishedEvent() {
        // Given
        MessageId messageId = new MessageId();
        Message message = messageFor(
                new MessagePublished(messageId, CONTENT, AUTHOR_ID)
        );

        // When
        message.republish(USER_ID, eventPublisher, AUTHOR_ID, CONTENT);

        // Then
        MessageRepublished expectedEvent = new MessageRepublished(messageId, USER_ID, AUTHOR_ID, CONTENT);
        assertThat(eventPublisher.publishedEvents).extracting("messageId").containsExactly(expectedEvent.getMessageId());
        assertThat(eventPublisher.publishedEvents).extracting("userId").containsExactly(expectedEvent.getUserId());
    }

    @Test
    public void whenAMessageIsRepublishedByItsAuthorThenItShouldNotSendRepublishedEvent() {
        // Given
        MessageId messageId = new MessageId();

        Message message = messageFor(
                new MessagePublished(messageId, CONTENT, AUTHOR_ID)
        );

        // When
        message.republish(AUTHOR_ID, eventPublisher, AUTHOR_ID, CONTENT);

        // Then
        assertThat(eventPublisher.publishedEvents).isEmpty();
    }

    @Test
    public void whenAMessageIsRepublishedTwiceByTheSameUserThenItShouldNotSendMessageRepublishedEvent() {
        // Given
        MessageId messageId = new MessageId();
        Message message = messageFor(
                new MessagePublished(messageId, CONTENT, AUTHOR_ID),
                new MessageRepublished(messageId, USER_ID, AUTHOR_ID, CONTENT)
        );

        // When
        message.republish(USER_ID, eventPublisher, AUTHOR_ID, CONTENT);

        // Then
        assertThat(eventPublisher.publishedEvents).isEmpty();
    }

    @Test
    public void whenAMessageIsDeletedByItsAuthorThenItShouldSendMessageDeletedEvent() {
        // Given
        MessageId messageId = new MessageId();
        List<Event> eventHistory = history(
                new MessagePublished(messageId, CONTENT, AUTHOR_ID)
        );

        Message message = new Message(eventHistory);

        // When
        message.delete(AUTHOR_ID, eventPublisher);

        // Then
        MessageDeleted expectedEvent = new MessageDeleted(messageId);
        assertThat(eventPublisher.publishedEvents).extracting("messageId").containsExactly(expectedEvent.getMessageId());
    }

    @Test
    public void whenAMessageIsDeletedBySomeoneElseThenItShouldNotSendMessageDeletedEvent() {
        // Given
        MessageId messageId = new MessageId();
        List<Event> eventHistory = history(
                new MessagePublished(messageId, CONTENT, AUTHOR_ID)
        );

        Message message = new Message(eventHistory);

        // When
        message.delete(RANDOM_GUY, eventPublisher);

        // Then
        assertThat(eventPublisher.publishedEvents).isEmpty();
    }

    @Test
    public void whenAMessageIsDeletedTwiceThenItShouldNotSendMessageDeletedEvent() {
        // Given
        MessageId messageId = new MessageId();
        Message message = messageFor(
                new MessagePublished(messageId, CONTENT, AUTHOR_ID),
                new MessageDeleted(messageId)
        );

        // When
        message.delete(AUTHOR_ID, eventPublisher);

        // Then
        assertThat(eventPublisher.publishedEvents).isEmpty();
    }

    @Test
    public void whenADeletedMessageIsRepublishedThenItShouldNotSendMessageRepublishedEvent() {
        // Given
        MessageId messageId = new MessageId();
        Message message = messageFor(
                new MessagePublished(messageId, CONTENT, AUTHOR_ID),
                new MessageDeleted(messageId)
        );

        // When
        message.republish(RANDOM_GUY, eventPublisher, AUTHOR_ID, CONTENT);

        // Then
        assertThat(eventPublisher.publishedEvents).isEmpty();
    }

    @Test
    public void WhenReplyThenRaiseReplyMessagePublished() {
        MessageId originalMessageId = new MessageId();
        Message message = messageFor(
                new MessagePublished(originalMessageId, CONTENT, AUTHOR_ID)
        );

        // When
        message.reply(REPLIER_ID, originalMessageId, AUTHOR_ID, REPLY_CONTENT, eventPublisher);

        //Then
        assertThat(eventPublisher.publishedEvents).extracting("originalMessageId").containsExactly(originalMessageId);
        assertThat(eventPublisher.publishedEvents).extracting("authorId").containsExactly(AUTHOR_ID);
        assertThat(eventPublisher.publishedEvents).extracting("replierId").containsExactly(REPLIER_ID);
        assertThat(eventPublisher.publishedEvents).extracting("message").containsExactly(REPLY_CONTENT);
    }

    @Test
    public void GivenReplyMessageWhenGetIdHasReplyMessageId() {
        //Given
        MessageId originalMessageId = new MessageId();
        MessageId replyMessageId = new MessageId();
        Message message = messageFor(
                new MessageReplied(AUTHOR_ID, REPLIER_ID, REPLY_CONTENT, originalMessageId, replyMessageId)
        );

        // When
        MessageId id = message.getId();

        // Then
        assertThat(id).isEqualTo(replyMessageId);
    }

    @Test
    public void GivenADeletedMessageWhenReplyThenDoNotRaiseMessageDeleted() {
        // Given
        MessageId originalMessageId = new MessageId();
        Message message = messageFor(
                new MessagePublished(originalMessageId, CONTENT, AUTHOR_ID),
                new MessageDeleted(originalMessageId)
        );

        // When
        message.reply(REPLIER_ID, originalMessageId, AUTHOR_ID, REPLY_CONTENT, eventPublisher);

        //Then
        assertThat(eventPublisher.publishedEvents).isEmpty();
    }

}


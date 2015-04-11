package mixter.domain.core.message;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageIdTest {
    @Test
    public void whenCreatingFromStringThenIdIsString() throws Exception {
        //Given
        String id = "string";
        //When
        MessageId messageId = new MessageId(id);
        //Then
        assertThat(messageId.getId()).isEqualTo(id);
    }

    @Test
    public void whenInstanciatingTwoMessageIdThenTheyHaveDifferentIds() {
        MessageId messageId1 = new MessageId();
        MessageId messageId2 = new MessageId();

        assertThat(messageId1).isNotEqualTo(messageId2);
    }

    @Test
    public void whenDisplayingMessageAsStringThenItDisplaysId() {
        MessageId messageId = new MessageId();

        assertThat(messageId.toString()).isEqualTo(messageId.getId());
    }
}

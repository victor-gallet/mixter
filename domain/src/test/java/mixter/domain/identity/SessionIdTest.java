package mixter.domain.identity;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class SessionIdTest {
    @Test
    public void GenerateTwiceShouldNotBeEqual() {
        SessionId sessionId1= SessionId.generate();
        SessionId sessionId2= SessionId.generate();

        assertThat(sessionId1).isNotEqualTo(sessionId2);
    }

    @Test
    public void shouldHaveValueAsToString() throws Exception {
        String value = "test";
        SessionId sessionId = new SessionId(value);
        assertThat(sessionId.toString()).isEqualTo(value);
    }


    @Test
    public void shouldBeEqualIfTheyHaveTheSameValue() throws Exception {
        String value = "test";
        SessionId sessionId1 = new SessionId(value);
        SessionId sessionId2 = new SessionId(value);
        assertThat(sessionId1).isEqualTo(sessionId2);
    }
}

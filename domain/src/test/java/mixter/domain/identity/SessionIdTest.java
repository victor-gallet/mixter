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
}

package mixter.domain.identity;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class SessionProjectionTest {

    public static final String EMAIL = "user@mix-it.fr";
    public static final UserId USER = new UserId(EMAIL);

    @Test
    public void shouldHaveEmailWhenUserIdHasEMail() throws Exception {
        SessionProjection sessionProjection = new SessionProjection(new SessionId("sessionId"), USER, true);
        assertThat(sessionProjection.hasMail(EMAIL)).isTrue();
    }
}

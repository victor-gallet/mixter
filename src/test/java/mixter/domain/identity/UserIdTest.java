package mixter.domain.identity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;


public class UserIdTest {
    @Test
    public void should_be_equal_by_value() throws Exception {
        //Given
        UserId userId1 = new UserId("mail@mix-it.fr");
        UserId userId2 = new UserId("mail@mix-it.fr");
        //When
        boolean equals = userId1.equals(userId2);
        //Then
        assertThat(equals).isTrue();
    }

    @Test
    public void userIdToStringShouldReturnEmail() {
        // Given
        String email = "mail@mix-it.fr";
        UserId userId = new UserId(email);
        // When
        String actual = userId.toString();
        // Then
        assertThat(actual).isEqualTo(email);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void newUserIdWithEmptyStringThrowsUserEmailCannotBeEmpty() {
        // Given
        String email = "";
        thrown.expect(UserEmailCannotBeEmpty.class);
        // When
        new UserId(email);
    }
}

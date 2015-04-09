package mixter.infra.repositories;

import mixter.domain.identity.SessionId;
import mixter.domain.identity.SessionProjection;
import mixter.domain.identity.UserId;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class InMemorySessionProjectionRepositoryTest {

    public static final UserId USER_ID = new UserId("mail@mix-it.fr");

    @Test
    public void repositoryShouldHaveNoProjectionWhenNoUserConnected() throws Exception {
        InMemorySessionProjectionRepository repository = new InMemorySessionProjectionRepository();

        assertThat(repository.getById(SessionId.generate())).isEqualTo(Optional.empty());
    }

    @Test
    public void repositoryShouldStoreSessionProjection() {
        // Given
        SessionProjection sessionProjection = new SessionProjection(SessionId.generate(), USER_ID, true);
        InMemorySessionProjectionRepository repository = new InMemorySessionProjectionRepository();
        // When
        repository.save(sessionProjection);
        // Then
        assertThat(repository.getById(sessionProjection.getSessionId()).get()).isEqualTo(sessionProjection);
    }

    @Test
    public void repositoryShouldReplaceSessionProjection() {
        // Given
        SessionId sessionId = SessionId.generate();
        SessionProjection sessionProjection = new SessionProjection(sessionId, USER_ID, false);
        SessionProjection updatedSessionProjection= new SessionProjection(sessionId, USER_ID, true);
        InMemorySessionProjectionRepository repository = new InMemorySessionProjectionRepository();
        repository.save(sessionProjection);
        // When
        repository.replaceBy(updatedSessionProjection);
        // Then
        assertThat(repository.getById(sessionProjection.getSessionId()).get()).isEqualTo(updatedSessionProjection);
    }
    @Test
    public void repositoryShouldNotFindSessionsProjectionsWhichAreInactive() {
        // Given
        SessionProjection sessionProjection = new SessionProjection(SessionId.generate(), USER_ID, false);
        InMemorySessionProjectionRepository repository = new InMemorySessionProjectionRepository();
        repository.save(sessionProjection);
        // When
        Optional<SessionProjection> maybeSession = repository.getById(sessionProjection.getSessionId());
        // Then
        assertThat(maybeSession).isEqualTo(Optional.empty());
    }
}

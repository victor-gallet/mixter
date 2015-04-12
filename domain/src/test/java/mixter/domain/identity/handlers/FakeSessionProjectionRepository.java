package mixter.domain.identity.handlers;

import mixter.domain.identity.SessionId;
import mixter.domain.identity.SessionProjection;
import mixter.domain.identity.SessionProjectionRepository;
import org.assertj.core.util.Sets;

import java.util.Optional;
import java.util.Set;

public class FakeSessionProjectionRepository implements SessionProjectionRepository {

    private Set<SessionProjection> sessions = Sets.newHashSet();

    public Set<SessionProjection> getSessions() {
        return sessions;
    }

    @Override
    public void save(SessionProjection sessionProjection) {
        sessions.add(sessionProjection);
    }

    @Override
    public void replaceBy(SessionProjection sessionProjection) {
        sessions.removeIf(s -> s.getSessionId().equals(sessionProjection.getSessionId()));
        sessions.add(sessionProjection);
    }

    @Override
    public Optional<SessionProjection> getById(SessionId sessionId) {
        return Optional.empty();
    }
}

package mixter.domain.core.subscription;

import mixter.UserId;

import java.util.Set;

public interface FollowerRepository {
    Set<UserId> getFollowers(UserId authorId);
}

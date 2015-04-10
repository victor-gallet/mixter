package mixter.domain.core.subscription;

import mixter.doc.Repository;
import mixter.domain.identity.UserId;

import java.util.Set;

@Repository
public interface FollowerRepository {
    Set<UserId> getFollowers(UserId authorId);

    void saveFollower(UserId followee, UserId follower);

    void removeFollower(UserId followee, UserId follower);
}

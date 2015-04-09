package mixter.domain.core.subscription;

import mixter.domain.identity.UserId;
import org.assertj.core.util.Maps;
import org.assertj.core.util.Sets;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class FakeFollowerRepository implements FollowerRepository {
    Map<UserId, Set<UserId>> followers = Maps.newHashMap();

    public void add(UserId followee, UserId... followers) {
        Set<UserId> currentFollowers = this.followers.getOrDefault(followee, Sets.newHashSet());
        Collections.addAll(currentFollowers, followers);
        this.followers.put(followee, currentFollowers);
    }

    @Override
    public Set<UserId> getFollowers(UserId followee) {
        return this.followers.getOrDefault(followee, Sets.newHashSet());
    }
}

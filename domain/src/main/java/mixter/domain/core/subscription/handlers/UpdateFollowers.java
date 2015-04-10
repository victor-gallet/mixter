package mixter.domain.core.subscription.handlers;

import mixter.domain.core.subscription.FollowerRepository;
import mixter.domain.core.subscription.events.UserFollowed;
import mixter.domain.core.subscription.events.UserUnfollowed;

public class UpdateFollowers {
    private FollowerRepository repository;

    public UpdateFollowers(FollowerRepository repository) {
        this.repository = repository;
    }

    public void apply(UserFollowed event) {
        repository.saveFollower(event.getSubscriptionId().getFollowee(), event.getSubscriptionId().getFollower());
    }

    public void apply(UserUnfollowed event) {
        repository.removeFollower(event.getSubscriptionId().getFollowee(), event.getSubscriptionId().getFollower());
    }
}

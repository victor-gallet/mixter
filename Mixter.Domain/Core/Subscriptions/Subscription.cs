﻿using Mixter.Domain.Core.Subscriptions.Events;
using Mixter.Domain.Identity;

namespace Mixter.Domain.Core.Subscriptions
{
    public class Subscription
    {
        public static void FollowUser(IEventPublisher eventPublisher, UserId follower, UserId followee)
        {
            var userFollowed = new UserFollowed(new SubscriptionId(follower, followee));
            eventPublisher.Publish(userFollowed);
        }
    }
}

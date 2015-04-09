package mixter.domain.subscription;

public interface SubscriptionRepository {
    Subscription getById(SubscriptionId subscriptionId);
}

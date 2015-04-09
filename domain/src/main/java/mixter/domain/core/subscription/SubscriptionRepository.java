package mixter.domain.core.subscription;

public interface SubscriptionRepository {
    Subscription getById(SubscriptionId subscriptionId);
}

export enum SubscriptionTier {
    FREE = 'Free Tier',
    PRO = 'Pro Tier',
    ELITE = 'Elite Tier'
}
export namespace SubscriptionTier {
    export function values(): SubscriptionTier[] {
        return [
            SubscriptionTier.FREE,
            SubscriptionTier.PRO,
            SubscriptionTier.ELITE,
        ]
    }
}
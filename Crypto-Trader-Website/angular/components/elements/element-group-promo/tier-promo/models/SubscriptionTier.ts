export enum SubscriptionTier {
    FREE = 'Free Tier',
    PRO = 'Pro Tier',
    ULTIMATE = 'Ultimate Tier',
}
export namespace SubscriptionTier {
    export function values(): SubscriptionTier[] {
        return [
            SubscriptionTier.FREE,
            SubscriptionTier.PRO,
            SubscriptionTier.ULTIMATE,
        ];
    }
}

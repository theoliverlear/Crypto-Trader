// tier-promo.component.ts
import {Component, Input} from "@angular/core";
import {SubscriptionTier} from "./models/SubscriptionTier";
import {
    aiStarIcon,
    brainIcon,
    ImageAsset,
    stockIcon
} from "../../../../assets/imageAssets";
import {TagType} from "../../../../models/html/TagType";

@Component({
    selector: 'tier-promo',
    templateUrl: './tier-promo.component.html',
    styleUrls: ['./tier-promo.component.scss']
})
export class TierPromoComponent {
    @Input() tier: SubscriptionTier = SubscriptionTier.FREE;
    constructor() {
        
    }
    
    protected getImageAsset(): ImageAsset {
        switch (this.tier) {
            case SubscriptionTier.FREE:
                return stockIcon;
            case SubscriptionTier.PRO:
                return brainIcon;
            case SubscriptionTier.ELITE:
                return aiStarIcon;
            default:
                throw new Error("Invalid tier: " + this.tier);
        }
    }

    protected getDescription(): string {
        switch (this.tier) {
            case SubscriptionTier.FREE:
                return "A disciplined “buy low, sell high” algorithm with " +
                    "safeguards to avoid chasing volatility.";
            case SubscriptionTier.PRO:
                return "Models analyze large, diverse datasets to estimate " +
                    "multi‑term price direction and confidence.";
            case SubscriptionTier.ELITE:
                return "Combines price action with additional context " +
                    "(e.g., news sentiment) to inform entries and exits.";
        }
    }
    
    protected readonly TagType = TagType;
}

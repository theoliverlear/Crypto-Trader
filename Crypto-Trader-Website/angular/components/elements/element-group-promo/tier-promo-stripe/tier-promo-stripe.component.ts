// tier-promo-stripe.component.ts
import { Component } from "@angular/core";
import {SubscriptionTier} from "../tier-promo/models/SubscriptionTier";

@Component({
    selector: 'tier-promo-stripe',
    standalone: false,
    templateUrl: './tier-promo-stripe.component.html',
    styleUrls: ['./tier-promo-stripe.component.scss']
})
export class TierPromoStripeComponent {
    constructor() {
        
    }

    protected readonly SubscriptionTier = SubscriptionTier;
}

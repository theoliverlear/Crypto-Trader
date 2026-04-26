// tier-promo-stripe.component.ts
import { Component } from '@angular/core';

import { TagType } from '@theoliverlear/angular-suite';

import { SubscriptionTier } from '../tier-promo/models/SubscriptionTier';

@Component({
    selector: 'tier-promo-stripe',
    standalone: false,
    templateUrl: './tier-promo-stripe.component.html',
    styleUrls: ['./tier-promo-stripe.component.scss'],
})
export class TierPromoStripeComponent {
    protected readonly SubscriptionTier: typeof SubscriptionTier = SubscriptionTier;
    protected readonly TagType: typeof TagType = TagType;
}

// tier-promo.component.ts
import { Component, Input } from '@angular/core';

import { TagType } from '@theoliverlear/angular-suite';
import {
    aiStarIcon,
    brainIcon,
    ImageAsset,
    stockIcon,
} from '@assets/imageAssets';

import { SubscriptionTier } from './models/SubscriptionTier';

// TODO: Move to models directory.
export interface TierFeature {
    text: string;
    highlight?: boolean;
}

@Component({
    selector: 'tier-promo',
    standalone: false,
    templateUrl: './tier-promo.component.html',
    styleUrls: ['./tier-promo.component.scss'],
})
export class TierPromoComponent {
    @Input() public tier: SubscriptionTier = SubscriptionTier.FREE;
    constructor() {}

    protected getImageAsset(): ImageAsset {
        switch (this.tier) {
            case SubscriptionTier.FREE:
                return stockIcon;
            case SubscriptionTier.PRO:
                return brainIcon;
            case SubscriptionTier.ULTIMATE:
                return aiStarIcon;
            default:
                throw new Error(`Invalid tier: ${this.tier}`);
        }
    }

    protected getDescription(): string {
        switch (this.tier) {
            case SubscriptionTier.FREE:
                return (
                    'Start trading smarter — zero cost, zero risk. Our ' +
                    'rules-based algorithm handles entries and exits so ' +
                    'you don\'t have to watch charts.'
                );
            case SubscriptionTier.PRO:
                return (
                    'Machine learning models trained on diverse market ' +
                    'data predict price direction with confidence scores ' +
                    '— so your trades are data-driven.'
                );
            case SubscriptionTier.ULTIMATE:
                return (
                    'The full arsenal. Combines price action, news ' +
                    'sentiment, and AI context to find optimal entries ' +
                    'and exits across every market condition.'
                );
        }
    }

    protected getFeatures(): TierFeature[] {
        // TODO: Extract to assets.
        switch (this.tier) {
            case SubscriptionTier.FREE:
                return [
                    { text: 'Buy-low, sell-high algorithm' },
                    { text: 'Volatility safeguards' },
                    { text: '24/7 automated execution' },
                    { text: 'Paper trading included' },
                ];
            case SubscriptionTier.PRO:
                return [
                    { text: 'Everything in Free' },
                    { text: 'ML-powered price prediction' },
                    { text: 'Multi-timeframe analysis' },
                    { text: 'Confidence-scored signals' },
                    { text: '10-second trade cadence' },
                ];
            case SubscriptionTier.ULTIMATE:
                return [
                    { text: 'Everything in Pro' },
                    { text: 'News & sentiment analysis' },
                    { text: 'AI-powered trade context' },
                    { text: 'Advanced risk controls' },
                    { text: '5-second trade cadence' },
                    { text: 'Beast Mode performance', highlight: true },
                    { text: 'AI Chat assistant', highlight: true },
                ];
        }
    }

    // TODO: Call a backend endpoint to fetch tier prices dynamically.
    protected getPriceLabel(): string {
        switch (this.tier) {
            case SubscriptionTier.FREE:
                return '$0/mo';
            case SubscriptionTier.PRO:
                return '$25/mo';
            case SubscriptionTier.ULTIMATE:
                return '$50/mo';
        }
    }

    protected getCtaText(): string {
        switch (this.tier) {
            case SubscriptionTier.FREE:
                return 'Get Started Free';
            case SubscriptionTier.PRO:
                return 'Go Pro';
            case SubscriptionTier.ULTIMATE:
                return 'Go Ultimate';
        }
    }

    protected getTierClass(): string {
        switch (this.tier) {
            case SubscriptionTier.FREE:
                return 'tier-free';
            case SubscriptionTier.PRO:
                return 'tier-pro';
            case SubscriptionTier.ULTIMATE:
                return 'tier-ultimate';
        }
    }

    protected isRecommended(): boolean {
        return this.tier === SubscriptionTier.PRO;
    }

    protected isUltimate(): boolean {
        return this.tier === SubscriptionTier.ULTIMATE;
    }

    protected readonly TagType: typeof TagType = TagType;
}

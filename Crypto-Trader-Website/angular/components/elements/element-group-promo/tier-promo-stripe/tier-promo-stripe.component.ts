// tier-promo-stripe.component.ts
import { AfterViewInit, Component, ElementRef, HostBinding, OnDestroy } from "@angular/core";
import {SubscriptionTier} from "../tier-promo/models/SubscriptionTier";

@Component({
    selector: 'tier-promo-stripe',
    standalone: false,
    templateUrl: './tier-promo-stripe.component.html',
    styleUrls: ['./tier-promo-stripe.component.scss']
})
export class TierPromoStripeComponent implements AfterViewInit, OnDestroy {
    @HostBinding('class.in-view') inView = false;

    private observer?: IntersectionObserver;

    constructor(private readonly elRef: ElementRef<HTMLElement>) {}

    ngAfterViewInit(): void {
        if (typeof window !== 'undefined' && 'IntersectionObserver' in window) {
            this.observer = new IntersectionObserver((entries) => {
                for (const entry of entries) {
                    if (entry.isIntersecting) {
                        this.inView = true;
                        if (this.observer) {
                            this.observer.unobserve(entry.target);
                        }
                    }
                }
            }, { threshold: 0.2 });
            this.observer.observe(this.elRef.nativeElement);
        } else {
            this.inView = true;
        }
    }

    ngOnDestroy(): void {
        if (this.observer) {
            this.observer.disconnect();
        }
    }

    protected readonly SubscriptionTier = SubscriptionTier;
}

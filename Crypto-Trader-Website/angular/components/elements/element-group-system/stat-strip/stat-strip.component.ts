// stat-strip.component.ts
import { AfterViewInit, ChangeDetectorRef, Component, ElementRef, Input, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { NumberTweenService } from '@ui/number-tween.service';

/**
 * Data that is shown in the statistic.
 */
export interface StatItem {
    icon: string;
    label: string;
    value?: number;
    suffix?: string;
}

/**
 * Promotional component that shows important stats about the app.
 */
@Component({
    selector: 'stat-strip',
    standalone: false,
    templateUrl: './stat-strip.component.html',
    styleUrls: ['./stat-strip.component.scss'],
})
export class StatStripComponent implements AfterViewInit, OnDestroy {
    @Input() public items: StatItem[] = [];

    public displayValues: Map<number, number> = new Map();

    private observer?: IntersectionObserver;
    private readonly tweenSubs: Subscription[] = [];

    constructor(
        private readonly elRef: ElementRef<HTMLElement>,
        private readonly numberTween: NumberTweenService,
        private readonly cdr: ChangeDetectorRef,
    ) {}

    /**
     * Starts tweens when the component is initialized in view.
     */
    public ngAfterViewInit(): void {
        this.items.forEach((_: StatItem, i: number): void => {
            this.displayValues.set(i, 0);
        });

        if (typeof window !== 'undefined' && 'IntersectionObserver' in window) {
            this.observer = new IntersectionObserver(
                (entries: IntersectionObserverEntry[]): void => {
                    for (const entry of entries) {
                        if (entry.isIntersecting) {
                            this.startTweens();
                            if (this.observer) {
                                this.observer.unobserve(entry.target);
                            }
                        }
                    }
                },
                { threshold: 0.2 },
            );
            this.observer.observe(this.elRef.nativeElement);
        } else {
            this.startTweens();
        }
    }

    /**
     * Gets the display label for a stat item.
     * @param stat
     * @param index
     */
    public getDisplayLabel(stat: StatItem, index: number): string {
        if (stat.value != null) {
            return Math.round(this.displayValues.get(index) ?? 0) + (stat.suffix ?? '');
        }
        return stat.label;
    }

    private startTweens(): void {
        this.items.forEach((stat: StatItem, i: number): void => {
            if (stat.value != null) {
                const sub: Subscription = this.numberTween.animate(0, stat.value, 1500).subscribe({
                    next: (val: number): void => {
                        this.displayValues.set(i, val);
                        this.cdr.detectChanges();
                    },
                });
                this.tweenSubs.push(sub);
            }
        });
    }

    /**
     * Disposes of the observer and tween subscriptions when the component is
     * destroyed.
     */
    public ngOnDestroy(): void {
        if (this.observer) {
            this.observer.disconnect();
        }
        this.tweenSubs.forEach((sub: Subscription): void => sub.unsubscribe());
    }
}

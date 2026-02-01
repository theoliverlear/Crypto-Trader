// display-currency.component.ts
import {
    AfterViewInit,
    Component,
    HostBinding,
    Input,
    OnChanges,
    OnDestroy,
    OnInit,
    SimpleChanges,
} from '@angular/core';
import { interval, Subject, Subscription, takeUntil } from 'rxjs';

import {
    ElementSize,
    TagType,
    WebSocketCapable,
} from '@theoliverlear/angular-suite';
import { defaultChartProperties } from '@assets/chartAssets';
import { defaultCurrencyIcon, ImageAsset } from '@assets/imageAssets';
import { CurrencyValueWsService } from '@ws/currency-value-ws.service';
import { CurrencyDayPerformanceService } from '@http/currency/currency-day-performance.service';
import { CurrencyHistoryService } from '@http/currency/currency-history.service';
import { CurrencyFormatterService } from '@ui/currency-formatter.service';
import { NumberTweenService } from '@ui/number-tween.service';
import { ChartDisplayProperties, SparkPoint } from '@models/chart/types';
import {
    DisplayCurrency,
    HistoryPoint,
    PerformanceRating,
} from '@models/currency/types';

/** Displays a currency's price and performance.
 *
 */
@Component({
    selector: 'display-currency',
    standalone: false,
    templateUrl: './display-currency.component.html',
    styleUrls: ['./display-currency.component.scss'],
    providers: [CurrencyValueWsService],
})
export class DisplayCurrencyComponent
    implements OnChanges, OnInit, AfterViewInit, OnDestroy, WebSocketCapable
{
    @Input() protected currency: DisplayCurrency;
    protected imageAsset: ImageAsset = defaultCurrencyIcon;
    protected history: HistoryPoint[] = [];
    protected performance: PerformanceRating = {
        rating: 'neutral',
        changePercent: '0%',
    };
    protected currencyPrice: string = '';
    protected chartProperties: ChartDisplayProperties = defaultChartProperties;
    private previousNumericPrice: number = 0;
    private currentNumericPrice: number = 0;
    private priceAnimationSub: Subscription | null = null;

    /**
     *
     */
    @HostBinding('class.up-performance')
    public get isUpPerformance(): boolean {
        return this.performance.rating === 'up';
    }

    /**
     *
     */
    @HostBinding('class.down-performance')
    public get isDownPerformance(): boolean {
        return this.performance.rating === 'down';
    }

    constructor(
        private readonly currencyFormatter: CurrencyFormatterService,
        private readonly historyService: CurrencyHistoryService,
        private readonly dayPerformance: CurrencyDayPerformanceService,
        private readonly currencyValueWebSocket: CurrencyValueWsService,
        private readonly numberTween: NumberTweenService,
    ) {}

    public webSocketSubscriptions: Record<string, Subscription> = {};
    /**
     *
     */
    public initializeWebSockets(): void {
        this.currencyValueWebSocket.connect();
        this.webSocketSubscriptions['currency-value'] =
            this.currencyValueWebSocket.getMessages().subscribe({
                next: (message: string): void => {
                    const numeric: number = Number(message);
                    if (!isFinite(numeric)) {
                        return;
                    }
                    this.setCurrencyNumericPrice(numeric);
                },
            });
    }

    /**
     *
     */
    private setChartProperties(): void {
        const prices: SparkPoint[] = this.history.map(
            (point: HistoryPoint): SparkPoint => ({
                date: point.date,
                value: point.value,
            }),
        );
        this.chartProperties = {
            ...defaultChartProperties,
            margin: { ...defaultChartProperties.margin },
            data: prices,
        };
    }

    /** Initialize WebSockets on component initialization.
     *
     */
    public ngOnInit(): void {
        this.initializeWebSockets();
    }

    private readonly destroy$: Subject<void> = new Subject<void>();

    /**
     *
     */
    private continuouslyUpdatePrice(): void {
        this.currencyValueWebSocket.sendMessage(this.currency.currencyCode);
        interval(5000)
            .pipe(takeUntil(this.destroy$))
            .subscribe((): void => this.updatePrice());
    }

    /**
     *
     */
    private updatePrice(): void {
        this.currencyValueWebSocket.sendMessage(this.currency.currencyCode);
    }

    /**
     *
     * @param price
     */
    private setCurrencyPrice(price: string): void {
        if (this.currencyPrice === price) {
            return;
        }
        this.currencyPrice = price;
        const numeric: number = this.parseNumeric(price);
        if (isFinite(numeric)) {
            this.previousNumericPrice = this.currentNumericPrice || numeric;
            this.currentNumericPrice = numeric;
        }
    }

    /**
     *
     * @param to
     * @param durationMs
     */
    private setCurrencyNumericPrice(
        to: number,
        durationMs: number = 500,
    ): void {
        const from: number = this.getCurrentDisplayedNumeric();
        if (!isFinite(from) || from === 0) {
            this.currencyPrice = this.currencyFormatter.formatCurrency(to);
            this.previousNumericPrice = to;
            this.currentNumericPrice = to;
            return;
        }
        if (to === from) {
            return;
        }

        if (this.priceAnimationSub) {
            this.priceAnimationSub.unsubscribe();
            this.priceAnimationSub = null;
        }
        this.previousNumericPrice = from;
        this.currentNumericPrice = to;
        this.priceAnimationSub = this.numberTween
            .animate(from, to, durationMs)
            .subscribe((value: number): void => {
                this.currencyPrice =
                    this.currencyFormatter.formatCurrency(value);
            });
    }

    private getCurrentDisplayedNumeric(): number {
        if (this.currencyPrice) {
            const parsed: number = this.parseNumeric(this.currencyPrice);
            if (isFinite(parsed)) return parsed;
        }

        if (
            isFinite(this.currentNumericPrice) &&
            this.currentNumericPrice !== 0
        ) {
            return this.currentNumericPrice;
        }
        return this.currency ? Number(this.currency.value) : 0;
    }

    private parseNumeric(formatted: string): number {
        const cleanedValue: string = formatted.replace(/[^0-9.+-]/g, '');
        return Number(cleanedValue);
    }

    /**
     *
     */
    getCurrencyPrice(): string {
        if (this.currency) {
            return this.currencyFormatter.formatCurrency(this.currency.value);
        } else {
            return this.currencyFormatter.formatCurrency(0);
        }
    }

    /**
     *
     * @param changes
     */
    ngOnChanges(changes: SimpleChanges) {
        if (changes['currency']) {
            this.resolveImageAsset();
            this.fetchHistory();
            const initValue: string = this.getCurrencyPrice();
            this.currencyPrice = initValue;
            const initNumericValue: number = this.parseNumeric(initValue);
            if (isFinite(initNumericValue)) {
                this.previousNumericPrice = initNumericValue;
                this.currentNumericPrice = initNumericValue;
            }
        }
        if (changes['history']) {
            this.setChartProperties();
        }
        if (changes['performance']) {
            this.updatePerformance();
        }
    }

    /**
     *
     */
    ngAfterViewInit() {
        this.resolveImageAsset();
        this.updatePerformance();
        this.continuouslyUpdatePrice();
        // this.setChartProperties();
    }

    /**
     *
     */
    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();

        Object.values(this.webSocketSubscriptions).forEach((sub) =>
            sub?.unsubscribe(),
        );
        this.webSocketSubscriptions = {};
        if (this.priceAnimationSub) {
            this.priceAnimationSub.unsubscribe();
            this.priceAnimationSub = null;
        }
        try {
            this.currencyValueWebSocket.disconnect();
        } catch {
            console.error('Failed to disconnect from WebSocket');
        }
    }

    /**
     *
     */
    updatePerformance(): void {
        this.dayPerformance
            .getCurrencyDayPerformance(this.currency.currencyCode)
            .subscribe((performance: any) => {
                this.performance = performance;
            });
    }

    /**
     *
     */
    fetchHistory(): void {
        if (!this.isCurrencyInvalid()) {
            this.listenForCurrencyHistory();
        }
    }

    private listenForCurrencyHistory() {
        this.historyService
            .getHistory(this.currency.currencyCode, 24, 60)
            .subscribe((points) => {
                this.history = points;
                this.setChartProperties();
            });
    }

    private isCurrencyInvalid() {
        return !this.currency || !this.currency.currencyCode;
    }

    /**
     *
     */
    async resolveImageAsset(): Promise<void> {
        if (!this.currency) {
            return;
        }
        const imageAsset: ImageAsset = {
            src: this.currency.logoUrl,
            alt: this.currency.currencyName + ' logo',
        };
        const imageLoads: boolean = await this.imageLoads(imageAsset.src);
        if (imageLoads) {
            this.imageAsset = imageAsset;
        } else {
            this.imageAsset = defaultCurrencyIcon;
        }
    }

    /**
     *
     * @param src
     */
    async imageLoads(src: string): Promise<boolean> {
        return new Promise<boolean>((resolve) => {
            const image = new Image();
            image.onload = () => {
                resolve(true);
            };
            image.onerror = () => {
                resolve(false);
            };
            image.src = src;
        });
    }

    protected readonly TagType = TagType;
    protected readonly ElementSize = ElementSize;
}

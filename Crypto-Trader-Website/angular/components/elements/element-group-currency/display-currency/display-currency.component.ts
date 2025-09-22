// display-currency.component.ts
import {
    AfterViewInit,
    Component,
    HostBinding,
    Input,
    OnChanges,
    OnDestroy,
    OnInit,
    SimpleChanges
} from "@angular/core";
import {
    DisplayCurrency,
    HistoryPoint,
    PerformanceRating
} from "../../../../models/currency/types";
import {
    defaultCurrencyIcon,
    ImageAsset
} from "../../../../assets/imageAssets";
import {
    ElementSize,
    TagType,
    WebSocketCapable
} from "@theoliverlear/angular-suite";
import {
    CurrencyFormatterService
} from "../../../../services/ui/currency-formatter.service";
import {
    GetCurrencyHistoryService
} from "../../../../services/net/http/currency/get-currency-history.service";
import {
    CurrencyDayPerformanceService
} from "../../../../services/net/http/currency/currency-day-performance.service";
import {
    CurrencyValueWebsocketService
} from "../../../../services/net/websocket/currency-value-websocket.service";
import {Subscription} from "rxjs";
import {
    NumberTweenService
} from "../../../../services/ui/number-tween.service";

@Component({
    selector: 'display-currency',
    standalone: false,
    templateUrl: './display-currency.component.html',
    styleUrls: ['./display-currency.component.scss'],
    providers: [CurrencyValueWebsocketService]
})
export class DisplayCurrencyComponent implements OnChanges, OnInit, AfterViewInit, OnDestroy, WebSocketCapable {
    @Input() currency: DisplayCurrency;
    imageAsset: ImageAsset = defaultCurrencyIcon;
    history: HistoryPoint[] = [];
    performance: PerformanceRating = { rating: 'neutral', changePercent: '0%' };
    currencyPrice: string = "";
    private previousNumericPrice: number = 0;
    private currentNumericPrice: number = 0;
    private priceAnimationSub: Subscription | null = null;

    @HostBinding('class.up-performance') get isUpPerformance(): boolean {
        return this.performance && this.performance.rating === "up";
    }
    
    @HostBinding('class.down-performance') get isDownPerformance(): boolean {
        return this.performance && this.performance.rating === "down";
    }

    constructor(private currencyFormatter: CurrencyFormatterService,
                private historyService: GetCurrencyHistoryService,
                private dayPerformance: CurrencyDayPerformanceService,
                private currencyValueWebSocket: CurrencyValueWebsocketService,
                private numberTween: NumberTweenService) {}

    webSocketSubscriptions: Record<string, Subscription> = {};
    initializeWebSockets(): void {
        this.currencyValueWebSocket.connect();
        this.webSocketSubscriptions['currency-value'] = this.currencyValueWebSocket.getMessages().subscribe({
           next: (message) => {
               console.log('[WS][currency-value] message:', message);
               const numeric = Number(message);
               if (!isFinite(numeric)) {
                   return;
               }
               this.setCurrencyNumericPrice(numeric);
           }
        });
    }
    
    ngOnInit(): void {
        this.initializeWebSockets();
    }
    continuouslyUpdatePrice() {
        this.currencyValueWebSocket.sendMessage(this.currency.currencyCode);
        setInterval(() => {
            this.updatePrice();
        }, 5000);
    }

    updatePrice(): void {
        this.currencyValueWebSocket.sendMessage(this.currency.currencyCode);
    }
    
    setCurrencyPrice(price: string): void {
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

    setCurrencyNumericPrice(to: number, durationMs: number = 500): void {
        const from = this.getCurrentDisplayedNumeric();
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
        this.priceAnimationSub = this.numberTween.animate(from, to, durationMs).subscribe(value => {
            this.currencyPrice = this.currencyFormatter.formatCurrency(value);
        });
    }

    private getCurrentDisplayedNumeric(): number {
        if (this.currencyPrice) {
            const parsed = this.parseNumeric(this.currencyPrice);
            if (isFinite(parsed)) return parsed;
        }
        
        if (isFinite(this.currentNumericPrice) && this.currentNumericPrice !== 0) {
            return this.currentNumericPrice;
        }
        return this.currency ? Number(this.currency.value) : 0;
    }

    private parseNumeric(formatted: string): number {
        const cleanedValue: string = formatted.replace(/[^0-9.+-]/g, '');
        return Number(cleanedValue);
    }
    
    getCurrencyPrice(): string {
        if (this.currency) {
            return this.currencyFormatter.formatCurrency(this.currency.value);
        } else {
            return this.currencyFormatter.formatCurrency(0);
        }
    }
    
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
    }

    ngAfterViewInit() {
        this.resolveImageAsset();
        this.updatePerformance();
        this.continuouslyUpdatePrice();
    }

    ngOnDestroy(): void {
        Object.values(this.webSocketSubscriptions).forEach(sub => sub?.unsubscribe());
        this.webSocketSubscriptions = {};
        if (this.priceAnimationSub) {
            this.priceAnimationSub.unsubscribe();
            this.priceAnimationSub = null;
        }
        try { this.currencyValueWebSocket.disconnect?.(); } catch {}
    }
    
    updatePerformance(): void {
        console.log("Updating performance for ", this.currency.currencyCode);
        this.dayPerformance.getCurrencyDayPerformance(this.currency.currencyCode)
            .subscribe(
                (performance: any) => {
                    this.performance = performance;
                },
            )
    }

    fetchHistory(): void {
        if (!this.isCurrencyInvalid()) {
            this.listenForCurrencyHistory();
        }
    }

    private listenForCurrencyHistory() {
        this.historyService.getHistory(this.currency.currencyCode, 24, 60)
            .subscribe(points => {
                this.history = points;
            });
    }

    private isCurrencyInvalid() {
        return !this.currency || !this.currency.currencyCode;
    }

    async resolveImageAsset(): Promise<void> {
        if (!this.currency) { return; }
        const imageAsset: ImageAsset = {
            src: this.currency.logoUrl,
            alt: this.currency.currencyName + " logo",
        };
        const imageLoads: boolean = await this.imageLoads(imageAsset.src);
        if (imageLoads) {
            this.imageAsset = imageAsset;
        } else {
            this.imageAsset = defaultCurrencyIcon;
        }
    }

    async imageLoads(src: string): Promise<boolean> {
        return new Promise<boolean>((resolve) => {
            const image = new Image();
            image.onload = () => {
                resolve(true);
            }
            image.onerror = () => {
                resolve(false);
            }
            image.src = src;
        });
    }

    protected readonly TagType = TagType;
    protected readonly ElementSize = ElementSize;
}
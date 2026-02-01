// trade-checkout-details.component.ts
import {
    Component,
    EventEmitter,
    Input,
    OnChanges,
    OnDestroy,
    OnInit,
    Output,
    SimpleChange,
    SimpleChanges,
} from '@angular/core';
import { interval, Subject, Subscription, takeUntil } from 'rxjs';

import {
    ElementSize,
    InputType,
    TagType,
    WebSocketCapable,
} from '@theoliverlear/angular-suite';
import { CurrencyValueWsService } from '@ws/currency-value-ws.service';
import { TradeCheckoutService } from '@http/trade/trade-checkout.service';
import { CurrencyFormatterService } from '@ui/currency-formatter.service';
import { NumberTweenService } from '@ui/number-tween.service';
import { SharesFormatterService } from '@ui/shares-formatter.service';
import { DisplayCurrency } from '@models/currency/types';
import { TradeCheckout } from '@models/trade/TradeCheckout';
import { TradeCheckoutRequest } from '@models/trade/types';
import { OperationSuccessResponse } from '@models/types';

import { BuyType } from '../buy-type/models/BuyType';

@Component({
    selector: 'trade-checkout-details',
    templateUrl: './trade-checkout-details.component.html',
    styleUrls: ['./trade-checkout-details.component.scss'],
    standalone: false,
})
export class TradeCheckoutDetailsComponent
    implements WebSocketCapable, OnInit, OnDestroy, OnChanges
{
    @Input() displayCurrency: DisplayCurrency;
    numDollars: number = 0;
    numShares: number = 0;
    numFeeDollars: number = 0;

    youPayText: string = '';
    youGetText: string = '';

    private tradeCheckout: TradeCheckout = new TradeCheckout();
    @Output()
    tradeCheckoutChange: EventEmitter<TradeCheckout> =
        new EventEmitter<TradeCheckout>();
    protected buyType: BuyType = BuyType.DOLLARS;
    private priceAnimationSub: Subscription | null = null;
    private destroy$: Subject<void> = new Subject<void>();

    constructor(
        private currencyFormatter: CurrencyFormatterService,
        private sharesFormatter: SharesFormatterService,
        private currencyValueWebSocket: CurrencyValueWsService,
        private numberTween: NumberTweenService,
        private tradeCheckoutService: TradeCheckoutService,
    ) {}

    attemptCheckout(): void {
        try {
            const tradeRequest: TradeCheckoutRequest =
                this.tradeCheckout.getRequest();
            this.tradeCheckoutService.checkout(tradeRequest).subscribe({
                next: (response: OperationSuccessResponse): void => {
                    console.log('Trade checkout successful:', response);
                },
            });
        } catch (error) {
            console.error('Failed to create trade checkout request:', error);
            return;
        }
    }

    ngOnChanges(changes: SimpleChanges): void {
        const displayCurrencyChange: SimpleChange = changes['displayCurrency'];
        if (!displayCurrencyChange || !displayCurrencyChange.currentValue)
            return;
        this.displayCurrency =
            displayCurrencyChange.currentValue as DisplayCurrency;
        this.tradeCheckout.currencyCode = this.displayCurrency.currencyCode;
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();

        Object.values(this.webSocketSubscriptions).forEach(
            (sub: Subscription): void => sub?.unsubscribe(),
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

    ngOnInit(): void {
        // this.youPayText = this.getYouPayText();
        // this.youGetText = this.getYouGetText();
        this.updateTradeTexts();
        this.initializeWebSockets();
        this.continuouslyUpdatePrice();
    }

    webSocketSubscriptions: Record<string, Subscription> = {};
    initializeWebSockets(): void {
        this.currencyValueWebSocket.connect();
        this.webSocketSubscriptions['currency-value'] =
            this.currencyValueWebSocket.getMessages().subscribe({
                next: (message: string): void => {
                    // if (!message) {
                    //     return;
                    // }
                    const numericPrice: number = Number(message);
                    // if (!isFinite(numericPrice)) {
                    //     return;
                    // }
                    this.updatePriceDisplay(numericPrice);
                },
            });
    }

    updatePriceDisplay(to: number, durationMs: number = 500): void {
        const previousPrice: number = this.displayCurrency.value;
        if (!isFinite(previousPrice) || previousPrice === 0) {
            this.displayCurrency.value = to;
            this.updateTradeTexts();
            return;
        }
        if (to === previousPrice) {
            this.updateTradeTexts();
            return;
        }

        if (this.priceAnimationSub) {
            this.priceAnimationSub.unsubscribe();
            this.priceAnimationSub = null;
        }
        this.priceAnimationSub = this.numberTween
            .animate(previousPrice, to, durationMs)
            .subscribe((value: number): void => {
                this.displayCurrency.value = value;
                this.updateTradeTexts();
            });
    }

    setCurrencyPrice(currencyPrice: number): void {
        this.displayCurrency.value = currencyPrice;
    }

    continuouslyUpdatePrice(): void {
        this.updatePrice();
        interval(5000)
            .pipe(takeUntil(this.destroy$))
            .subscribe((): void => this.updatePrice());
    }

    updatePrice(): void {
        this.currencyValueWebSocket.sendMessage(
            this.displayCurrency.currencyCode,
        );
    }

    hasVendorFee(): boolean {
        return this.numFeeDollars > 0;
    }

    setFromInput(input: string): void {
        const inputAsNumber: number = Number(input);
        if (isNaN(inputAsNumber)) {
            return;
        }
        if (this.buyType === BuyType.DOLLARS) {
            this.numDollars = inputAsNumber;
            this.tradeCheckout.numDollars = inputAsNumber;
        } else {
            this.numShares = inputAsNumber;
            this.tradeCheckout.numShares = inputAsNumber;
        }
        this.updateTradeTexts();
    }

    setBuyType(buyType: BuyType): void {
        if (this.buyType === BuyType.DOLLARS) {
            this.numShares = this.numDollars;
            this.tradeCheckout.numShares = this.numDollars;
            this.numDollars = 0;
            this.tradeCheckout.numShares = 0;
        } else {
            this.numDollars = this.numShares;
            this.tradeCheckout.numDollars = this.numShares;
            this.numShares = 0;
            this.tradeCheckout.numShares = 0;
        }
        this.buyType = buyType;
        this.updateTradeTexts();
    }

    private updateTradeTexts(): void {
        this.youPayText = this.getYouPayText();
        this.youGetText = this.getYouGetText();
    }

    getVendorFee(): string {
        // TODO: Implement vendor fee fetching from API.
        return '';
    }

    getYouPayText(): string {
        if (this.buyType === BuyType.SHARES) {
            const numDollars: number = this.getSharesValue();
            return this.currencyFormatter.formatCurrency(numDollars);
        } else {
            return this.currencyFormatter.formatCurrency(this.numDollars);
        }
    }

    private getSharesValue(): number {
        return this.displayCurrency.value * this.numShares;
    }

    getYouGetText(): string {
        if (this.buyType === BuyType.SHARES) {
            return this.sharesFormatter.formatShares(
                this.numShares,
                this.displayCurrency.currencyCode,
            );
        } else {
            const numShares: number =
                this.numDollars / this.displayCurrency.value;
            return this.sharesFormatter.formatShares(
                numShares,
                this.displayCurrency.currencyCode,
            );
        }
    }

    getNumDecimals(numShares: number): number {
        return numShares < 1 ? 10 : 2;
    }

    getPlaceholderText(): string {
        if (this.buyType === BuyType.DOLLARS) {
            return '# of Dollars';
        } else {
            return '# of Shares';
        }
    }

    protected readonly TagType = TagType;
    protected readonly InputType = InputType;
    protected readonly ElementSize = ElementSize;
}

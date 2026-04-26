// currency-ticker-tile.component.ts
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

import { CurrencyFormatterService } from '@ui/currency-formatter.service';
import { DisplayCurrency, PerformanceRating } from '@models/currency/types';
import { CryptoTraderLoggerService } from '@services/logging/crypto-trader-logger.service';

@Component({
    selector: 'currency-ticker-tile',
    standalone: false,
    templateUrl: './currency-ticker-tile.component.html',
    styleUrls: ['./currency-ticker-tile.component.scss'],
})
export class CurrencyTickerTileComponent implements OnInit {
    // TODO: Remove non-null assertion.
    @Input() currency!: DisplayCurrency;
    @Input() performance: PerformanceRating = { rating: 'neutral', changePercent: '0%' };
    @Output() tileClick: EventEmitter<void> = new EventEmitter<void>();

    constructor(
        private readonly currencyFormatter: CurrencyFormatterService,
        private readonly logger: CryptoTraderLoggerService,
    ) {}

    public ngOnInit(): void {
        this.logger.debug(`CurrencyTickerTileComponent initialized for ${this.currency?.currencyCode}`, 'CurrencyTickerTile');
    }

    protected formatPrice(): string {
        return this.currencyFormatter.formatCurrency(this.currency.value);
    }

    protected onClick(): void {
        this.logger.info(`Currency ticker tile clicked: ${this.currency.currencyCode}`, 'CurrencyTickerTile');
        this.tileClick.emit();
    }
}

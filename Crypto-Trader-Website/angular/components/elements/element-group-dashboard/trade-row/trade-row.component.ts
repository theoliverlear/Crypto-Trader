// trade-row.component.ts
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

import { CurrencyFormatterService } from '@ui/currency-formatter.service';
import { TimeFormatterService } from '@ui/time-formatter.service';
import { TradeEvent } from '@models/trader/types';
import { CryptoTraderLoggerService } from '@services/logging/crypto-trader-logger.service';

@Component({
    selector: 'trade-row',
    standalone: false,
    templateUrl: './trade-row.component.html',
    styleUrls: ['./trade-row.component.scss'],
})
export class TradeRowComponent implements OnInit {
    // TODO: Remove non-null assertion.
    @Input() trade!: TradeEvent;
    @Output() rowClick: EventEmitter<void> = new EventEmitter<void>();

    constructor(
        private readonly currencyFormatter: CurrencyFormatterService,
        private readonly timeFormatter: TimeFormatterService,
        private readonly logger: CryptoTraderLoggerService,
    ) {}

    public ngOnInit(): void {
        this.logger.debug(`TradeRowComponent initialized for trade: ${this.trade.id}`, 'TradeRow');
    }

    protected formatValue(): string {
        const prefix: string = this.trade.valueChange >= 0 ? '+' : '';
        return `${prefix}${this.currencyFormatter.formatCurrency(Math.abs(this.trade.valueChange))}`;
    }

    protected formatTime(): string {
        return this.timeFormatter.formatTime(this.trade.tradeTime);
    }

    protected onClick(): void {
        this.logger.info(`Trade row clicked: ${this.trade.id}`, 'TradeRow');
        this.rowClick.emit();
    }
}

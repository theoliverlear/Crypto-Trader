// trade.component.ts
import { Component, OnInit } from '@angular/core';

import { CryptoTraderLoggerService } from '@services/logging/crypto-trader-logger.service';

@Component({
    selector: 'trade',
    templateUrl: './trade.component.html',
    styleUrls: ['./trade.component.scss'],
    standalone: false,
})
export class TradeComponent implements OnInit {
    constructor(private readonly log: CryptoTraderLoggerService) {}

    ngOnInit(): void {
        this.log.setContext('Trade');
        this.log.info('Trade component initialized');
    }
}

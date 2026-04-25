// statistics.component.ts
import { Component, OnInit } from '@angular/core';

import { CryptoTraderLoggerService } from '@services/logging/crypto-trader-logger.service';

@Component({
    selector: 'statistics',
    templateUrl: './statistics.component.html',
    styleUrls: ['./statistics.component.scss'],
    standalone: false,
})
export class StatisticsComponent implements OnInit {
    constructor(private readonly log: CryptoTraderLoggerService) {}

    ngOnInit(): void {
        this.log.setContext('Statistics');
        this.log.info('Statistics component initialized');
    }
}

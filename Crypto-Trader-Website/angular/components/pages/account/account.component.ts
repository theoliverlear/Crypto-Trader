// account.component.ts
import { Component, OnInit } from '@angular/core';

import { CryptoTraderLoggerService } from '@services/logging/crypto-trader-logger.service';

@Component({
    selector: 'account',
    standalone: false,
    templateUrl: './account.component.html',
    styleUrls: ['./account.component.scss'],
})
export class AccountComponent implements OnInit {
    constructor(private readonly log: CryptoTraderLoggerService) {}

    ngOnInit(): void {
        this.log.setContext('Account');
        this.log.info('Account component initialized');
    }
}

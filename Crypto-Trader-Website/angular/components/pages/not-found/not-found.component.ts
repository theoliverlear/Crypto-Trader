// not-found.component.ts
import { Component, OnInit } from '@angular/core';

import { CryptoTraderLoggerService } from '@services/logging/crypto-trader-logger.service';

@Component({
    selector: 'not-found',
    standalone: false,
    templateUrl: './not-found.component.html',
    styleUrls: ['./not-found.component.scss'],
})
export class NotFoundComponent implements OnInit {
    constructor(private readonly logger: CryptoTraderLoggerService) {}

    public ngOnInit(): void {
        this.logger.info('NotFoundComponent initialized.', 'NotFound');
    }
}

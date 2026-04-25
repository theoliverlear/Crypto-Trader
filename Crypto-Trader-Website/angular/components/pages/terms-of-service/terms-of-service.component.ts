// terms-of-service.component.ts
import { Component, OnInit } from '@angular/core';

import { CryptoTraderLoggerService } from '@services/logging/crypto-trader-logger.service';

@Component({
    selector: 'terms-of-service',
    standalone: false,
    templateUrl: './terms-of-service.component.html',
    styleUrls: ['./terms-of-service.component.scss'],
})
export class TermsOfServiceComponent implements OnInit {
    constructor(private readonly logger: CryptoTraderLoggerService) {}

    public ngOnInit(): void {
        this.logger.info('TermsOfServiceComponent initialized.', 'TermsOfService');
    }
}

import { Component, OnInit } from '@angular/core';

import { AuthPopup, WebSocketCapable } from '@theoliverlear/angular-suite';
import { CryptoTraderLoggerService } from '@services/logging/crypto-trader-logger.service';

@Component({
    selector: 'authorize',
    standalone: false,
    templateUrl: './authorize.component.html',
    styleUrls: ['./authorize.component.scss'],
})
export class AuthorizeComponent implements OnInit {
    authPopup: AuthPopup = AuthPopup.NONE;
    constructor(private readonly log: CryptoTraderLoggerService) {}

    ngOnInit(): void {
        this.log.setContext('Authorize');
        this.log.info('Authorize component initialized');
    }

    setAuthPopup(authPopup: AuthPopup): void {
        this.log.debug(`Setting auth popup to ${authPopup}`);
        this.authPopup = authPopup;
    }

    protected readonly AuthPopup = AuthPopup;
}

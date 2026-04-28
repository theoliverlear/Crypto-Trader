// account.component.ts
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { CryptoTraderLoggerService } from '@services/logging/crypto-trader-logger.service';
import { DeleteAccountService } from '@http/user/delete-account.service';
import { LogoutService } from '@http/auth/access/logout.service';
import { TokenStorageService } from '@services/auth/token-storage.service';
import { DpopKeyService } from '@services/auth/dpop/dpop-key.service';

@Component({
    selector: 'account',
    standalone: false,
    templateUrl: './account.component.html',
    styleUrls: ['./account.component.scss'],
})
export class AccountComponent implements OnInit {
    showDeleteConfirmation: boolean = false;

    constructor(
        private readonly log: CryptoTraderLoggerService,
        private readonly deleteAccountService: DeleteAccountService,
        private readonly logoutService: LogoutService,
        private readonly tokenStorageService: TokenStorageService,
        private readonly dpopKeyService: DpopKeyService,
        private readonly router: Router,
    ) {}

    ngOnInit(): void {
        this.log.setContext('Account');
        this.log.info('Account component initialized');
    }

    /** Show the "Are you sure?" confirmation dialog. */
    onDeleteAccountClick(): void {
        this.showDeleteConfirmation = true;
    }

    /** Cancel account deletion and hide the confirmation dialog. */
    onCancelDelete(): void {
        this.showDeleteConfirmation = false;
    }

    /** Confirm account deletion: delete the account, log out, and navigate to the auth page. */
    onConfirmDelete(): void {
        this.deleteAccountService.deleteAccount().subscribe({
            next: (): void => {
                this.log.info('Account deleted successfully');
                this.performLogoutAndRedirect();
            },
            error: (error): void => {
                this.log.error('Failed to delete account', error);
                this.showDeleteConfirmation = false;
            },
        });
    }

    /** Clear local tokens and navigate to the authorize page. */
    private performLogoutAndRedirect(): void {
        this.logoutService.logout().subscribe({
            error: (error): void => {
                this.log.warn('Logout request error after account deletion (ignored):', error);
            },
            complete: (): void => {
                try {
                    this.tokenStorageService.clear();
                    this.dpopKeyService.clear();
                } catch {
                    this.log.error('Failed to clear token storage after account deletion');
                }
                void this.router.navigate(['/authorize']).then((): void => {
                    this.log.info('Account deletion and logout complete');
                });
            },
        });
    }
}

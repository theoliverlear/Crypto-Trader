// account.component.ts
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { CryptoTraderLoggerService } from '@services/logging/crypto-trader-logger.service';
import { DeleteAccountService } from '@http/user/delete-account.service';
import { TokenStorageService } from '@auth/token-storage.service';

@Component({
    selector: 'account',
    standalone: false,
    templateUrl: './account.component.html',
    styleUrls: ['./account.component.scss'],
})
export class AccountComponent implements OnInit {
    protected showDeleteConfirmDialog: boolean = false;

    constructor(
        private readonly log: CryptoTraderLoggerService,
        private readonly deleteAccountService: DeleteAccountService,
        private readonly tokenStorage: TokenStorageService,
        private readonly router: Router,
    ) {}

    ngOnInit(): void {
        this.log.setContext('Account');
        this.log.info('Account component initialized');
    }

    protected onDeleteAccountClick(): void {
        this.showDeleteConfirmDialog = true;
    }

    protected onDeleteConfirm(): void {
        this.showDeleteConfirmDialog = false;
        this.deleteAccountService.deleteAccount().subscribe({
            next: () => {
                this.log.info('Account deleted successfully');
                this.tokenStorage.clear();
                void this.router.navigate(['/']);
            },
            error: (error) => {
                this.log.error('Failed to delete account', error);
            },
        });
    }

    protected onDeleteCancel(): void {
        this.showDeleteConfirmDialog = false;
    }
}

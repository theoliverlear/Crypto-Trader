// allocation-badge.component.ts
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

import { CryptoTraderLoggerService } from '@services/logging/crypto-trader-logger.service';

@Component({
    selector: 'allocation-badge',
    standalone: false,
    templateUrl: './allocation-badge.component.html',
    styleUrls: ['./allocation-badge.component.scss'],
})
export class AllocationBadgeComponent implements OnInit {
    @Input() label: string = '';
    @Input() value: string = '';
    // TODO: Extract type.
    @Input() variant: 'cash' | 'crypto' | 'assets' = 'cash';
    @Output() badgeClick: EventEmitter<void> = new EventEmitter<void>();

    constructor(private readonly logger: CryptoTraderLoggerService) {}

    public ngOnInit(): void {
        this.logger.debug(`AllocationBadgeComponent initialized: ${this.label}`, 'AllocationBadge');
    }

    protected onClick(): void {
        this.logger.info(`Allocation badge clicked: ${this.label}`, 'AllocationBadge');
        this.badgeClick.emit();
    }
}

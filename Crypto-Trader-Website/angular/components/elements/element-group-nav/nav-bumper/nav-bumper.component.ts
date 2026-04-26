import { Component, OnInit } from '@angular/core';

import { LoggedInService } from '@http/auth/status/logged-in.service';
import { MySubscriptionTierService } from '@http/user/my-subscription-tier.service';
import { SubscriptionTier } from '@models/user/types';

@Component({
    selector: 'nav-bumper',
    templateUrl: './nav-bumper.component.html',
    styleUrls: ['./nav-bumper.component.scss'],
    standalone: false,
})
export class NavBumperComponent implements OnInit {
    protected isLoggedIn: boolean = false;
    protected isUltimate: boolean = false;
    constructor(
        private readonly loggedInService: LoggedInService,
        private readonly mySubscriptionTierService: MySubscriptionTierService,
    ) {}

    public ngOnInit(): void {
        this.listenForAuthStatus();
        this.listenForSubscriptionTier();
    }

    private listenForAuthStatus(): void {
        this.loggedInService.getAuthState().subscribe((authStatus: boolean): void => {
            this.isLoggedIn = authStatus;
        });
    }

    private listenForSubscriptionTier(): void {
        this.mySubscriptionTierService.getSubscriptionTierStream().subscribe((tier: SubscriptionTier): void => {
            this.isUltimate = tier === 'ULTIMATE';
        });
    }
}

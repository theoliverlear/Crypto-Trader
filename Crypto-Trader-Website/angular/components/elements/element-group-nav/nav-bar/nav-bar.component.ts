import { Component, OnInit } from '@angular/core';

import { TextElementLink } from '@theoliverlear/angular-suite';
import {
    navBarCurrenciesTextLink,
    navBarHomeLink,
    navBarPortfolioTextLink,
    navBarTraderTextLink,
} from '@assets/elementLinkAssets';
import { LoggedInService } from '@http/auth/status/logged-in.service';

import { NavBarItemOption } from '../nav-bar-item/models/NavBarItemOption';

/** A navigation bar that contains links to different pages.
 *
 */
@Component({
    selector: 'nav-bar',
    standalone: false,
    templateUrl: './nav-bar.component.html',
    styleUrls: ['./nav-bar.component.scss'],
})
export class NavBarComponent implements OnInit {
    protected isLoggedIn: boolean = false;
    constructor(private readonly loggedInService: LoggedInService) {}

    /** On init, listen for auth status changes and verify login status.
     *
     */
    public ngOnInit(): void {
        this.listenForAuthStatus();
        this.verifyLoginStatus();
    }

    private listenForAuthStatus(): void {
        this.loggedInService.getAuthState().subscribe((authStatus: boolean): void => {
            this.isLoggedIn = authStatus;
        });
    }

    /** Verify login status by subscribing to the logged in service.
     *
     */
    public verifyLoginStatus(): void {
        console.log('Verifying login status...');
        this.loggedInService.isLoggedIn().subscribe();
    }

    protected readonly navBarHomeLink: TextElementLink = navBarHomeLink;
    protected readonly navBarPortfolioLink: TextElementLink = navBarPortfolioTextLink;
    protected readonly navBarTraderLink: TextElementLink = navBarTraderTextLink;
    protected readonly navBarCurrenciesLink: TextElementLink = navBarCurrenciesTextLink;
    protected readonly NavBarItemOption: typeof NavBarItemOption = NavBarItemOption;
}

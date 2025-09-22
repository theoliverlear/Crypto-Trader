import {
    Component, OnInit
} from '@angular/core';
import {
    navBarCurrenciesTextLink,
    navBarHomeLink,
    navBarPortfolioTextLink,
    navBarTraderTextLink
} from "../../../../assets/elementLinkAssets";
import {
    LoggedInService
} from "../../../../services/net/http/auth/status/logged-in.service";
import {NavBarItemOption} from "../nav-bar-item/models/NavBarItemOption";


@Component({
    selector: 'nav-bar',
    standalone: false,
    templateUrl: './nav-bar.component.html',
    styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent implements OnInit {
    isLoggedIn: boolean = false;
    constructor(private loggedInService: LoggedInService) {
        
    }

    ngOnInit() {
        this.loggedInService.getAuthState().subscribe((authStatus: boolean) => {
            this.isLoggedIn = authStatus;
        });
        this.loggedInService.isLoggedIn().subscribe();
    }

    protected readonly navBarHomeLink = navBarHomeLink;
    protected readonly navBarPortfolioLink = navBarPortfolioTextLink;
    protected readonly navBarTraderLink = navBarTraderTextLink;
    protected readonly navBarCurrenciesLink = navBarCurrenciesTextLink;
    protected readonly NavBarItemOption = NavBarItemOption;
}

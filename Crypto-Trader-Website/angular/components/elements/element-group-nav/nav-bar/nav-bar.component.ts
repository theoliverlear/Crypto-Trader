import {
    Component
} from '@angular/core';
import {
    navBarCurrenciesLink,
    navBarHomeLink,
    navBarPortfolioLink,
    navBarTraderLink
} from "../../../../assets/elementLinkAssets";


@Component({
    selector: 'nav-bar',
    templateUrl: './nav-bar.component.html',
    styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent {
    constructor() {
        console.log('NavBarComponent loaded');
    }

    protected readonly navBarHomeLink = navBarHomeLink;
    protected readonly navBarPortfolioLink = navBarPortfolioLink;
    protected readonly navBarTraderLink = navBarTraderLink;
    protected readonly navBarCurrenciesLink = navBarCurrenciesLink;
}

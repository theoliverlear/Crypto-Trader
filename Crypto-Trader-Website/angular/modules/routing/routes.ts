import {Route, Routes} from "@angular/router";
import {HomeComponent} from "../../components/pages/home/home.component";
import {
    PortfolioComponent
} from "../../components/pages/portfolio/portfolio.component";
import {AuthGuard} from "../../services/guard/auth.guard";
import {
    AccountComponent
} from "../../components/pages/account/account.component";
import {
    AuthorizeComponent
} from "../../components/pages/authorize/authorize.component";
import {
    TraderComponent
} from "../../components/pages/trader/trader.component";
import {
    TermsOfServiceComponent
} from "../../components/pages/terms-of-service/terms-of-service.component";
import {
    CurrenciesComponent
} from "../../components/pages/currencies/currencies.component";
import {
    ConsoleComponent
} from "../../components/pages/console/console.component";


const isDevelopment = false;


export const accountRoute: Route = {
    path: 'account',
    component: AccountComponent,
    // canActivate: isDevelopment ? [] : [AuthGuard],
    canActivate: [AuthGuard],
    data: {
        meta: {
            title: 'Account | Crypto Trader'
        },
    }
};
export const authorizeRoute: Route = {
    path: 'authorize',
    component: AuthorizeComponent,
    data: {
        meta: {
            title: 'Authorize | Crypto Trader'
        },
    }
};
export const homeRoute: Route = {
    path: '',
    component: HomeComponent,
    data: {
        meta: {
            title: 'Crypto Trader'
        },
    }
};
export const portfolioRoute: Route = {
    path: 'portfolio',
    component: PortfolioComponent,
    canActivate: isDevelopment ? [] : [AuthGuard],
    data: {
        meta: {
            title: 'Portfolio | Crypto Trader'
        },
    }
};
export const termsOfServiceRoute: Route = {
    path: 'terms',
    component: TermsOfServiceComponent,
    data: {
        meta: {
            title: 'Terms of Service | Crypto Trader'
        },
    }
}
export const traderRoute: Route = {
    path: 'trader',
    component: TraderComponent,
    canActivate: isDevelopment ? [] : [AuthGuard],
    data: {
        meta: {
            title: 'Trader | Crypto Trader'
        },
    }
};

export const currenciesRoute: Route = {
    path: 'currencies',
    component: CurrenciesComponent,
    data: {
        meta: {
            title: 'Currencies | Crypto Trader'
        },
    }
};

export const consoleRoute: Route = {
    path: 'console',
    component: ConsoleComponent,
    canActivate: isDevelopment ? [] : [AuthGuard],
    data: {
        meta: {
            title: 'Console | Crypto Trader'
        },
    }
};
export const routes: Routes = [
    accountRoute,
    authorizeRoute,
    homeRoute,
    portfolioRoute,
    termsOfServiceRoute,
    currenciesRoute,
    consoleRoute,
    traderRoute
];
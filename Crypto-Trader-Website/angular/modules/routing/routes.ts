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
import {TradeComponent} from "../../components/pages/trade/trade.component";
import {AccountGuard} from "../../services/guard/account.guard";
import {DashboardGuard} from "../../services/guard/dashboard.guard";
import {
    DashboardComponent
} from "../../components/pages/dashboard/dashboard.component";


const isDevelopment = true;


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
    canActivate: isDevelopment ? [] : [AccountGuard],
    data: {
        meta: {
            title: 'Authorize | Crypto Trader'
        },
    }
};
export const homeRoute: Route = {
    path: '',
    component: HomeComponent,
    canActivate: isDevelopment ? [] : [DashboardGuard],
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
export const tradeRoute: Route = {
    path: 'trade',
    component: TradeComponent,
    canActivate: isDevelopment ? [] : [AuthGuard],
    data: {
        meta: {
            title: 'Trade | Crypto Trader'
        },
    }
};

export const dashboardRoute: Route = {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [],
    data: {
        meta: {
            title: 'Dashboard | Crypto Trader'
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
    traderRoute,
    tradeRoute,
    dashboardRoute,
];
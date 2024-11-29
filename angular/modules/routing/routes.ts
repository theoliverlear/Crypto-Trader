import {Route, Routes} from "@angular/router";
import {HomeComponent} from "../../components/pages/home/home.component";
import {
    PortfolioComponent
} from "../../components/pages/portfolio/portfolio.component";
import {AuthGuard} from "../../services/guard/auth.guard";


const isDevelopment = true;

export const homeRoute: Route = {
    path: '',
    component: HomeComponent,
    data: {
        meta: {
            title: 'Crypto Trader'
        },
    }
}
export const portfolioRoute: Route = {
    path: 'portfolio',
    component: PortfolioComponent,
    canActivate: isDevelopment ? [] : [AuthGuard],
    data: {
        meta: {
            title: 'Portfolio | Crypto Trader'
        },
    }
}
export const routes: Routes = [
    homeRoute,
    portfolioRoute
];
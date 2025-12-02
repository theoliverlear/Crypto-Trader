import {Injectable} from "@angular/core";
import {Portfolio} from "../../../../models/portfolio/types";
import {HttpClientService} from "@theoliverlear/angular-suite";
import {environment} from "../../../../environments/environment";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class PortfolioService extends HttpClientService<any, Portfolio> {
    private static readonly URL: string = `${environment.apiUrl}/portfolio/get`;
    constructor() {
        super(PortfolioService.URL);
    }
    getPortfolio(): Observable<Portfolio> {
        return this.get();
    }
}
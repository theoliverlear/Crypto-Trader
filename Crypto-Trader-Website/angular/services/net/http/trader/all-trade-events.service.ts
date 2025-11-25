import {Injectable} from "@angular/core";
import {HttpClientService} from "@theoliverlear/angular-suite";
import { TradeEventList } from "../../../../models/trader/types";
import {environment} from "../../../../environments/environment";
import {Observable} from "rxjs";
@Injectable({
    providedIn: 'root'
})
export class AllTradeEventsService extends HttpClientService<any, TradeEventList> {
    private static URL: string  = `${environment.apiUrl}/trader/events/all`;

    constructor() {
        super(AllTradeEventsService.URL);
    }

    public getAllTradeEvents(): Observable<TradeEventList> {
        return this.get();
    }
}
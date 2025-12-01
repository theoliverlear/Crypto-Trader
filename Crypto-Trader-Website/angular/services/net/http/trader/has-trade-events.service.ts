import {HttpClientService} from "@theoliverlear/angular-suite";
import {environment} from "../../../../environments/environment";
import {Observable} from "rxjs";
import {Injectable} from "@angular/core";

@Injectable({
    providedIn: 'root'
})
export class HasTradeEventsService extends HttpClientService<any, boolean> {
    private static readonly URL: string = `${environment.apiUrl}/trader/events/exists`;
    constructor() {
        super(HasTradeEventsService.URL);
    }
    
    public hasTradeEvents(): Observable<boolean> {
        return this.get();
    }
}
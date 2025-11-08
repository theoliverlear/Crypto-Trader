import { Injectable } from "@angular/core";
import { HttpClientService } from "@theoliverlear/angular-suite";
import {TradeCheckoutRequest} from "../../../../models/trade/types";
import {environment} from "../../../../environments/environment";
import {OperationSuccessResponse} from "../../../../models/types";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class TradeCheckoutService extends HttpClientService<TradeCheckoutRequest, OperationSuccessResponse> {
    private static readonly URL: string = `${environment.apiUrl}/trade/checkout`;
    constructor() {
        super(TradeCheckoutService.URL);
    }

    public checkout(request: TradeCheckoutRequest): Observable<OperationSuccessResponse> {
        return this.post(request);
    }
}

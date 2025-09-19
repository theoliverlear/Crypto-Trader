import {Injectable} from "@angular/core";
import {
    WebSocketService
} from "@theoliverlear/angular-suite";
import {environment} from "../../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class CurrencyValueWebsocketService extends WebSocketService<string, string> {
    private static readonly URL: string = `${environment.websocketUrl}/currency/value`;
    
    constructor() {
        super(CurrencyValueWebsocketService.URL);
    }
}
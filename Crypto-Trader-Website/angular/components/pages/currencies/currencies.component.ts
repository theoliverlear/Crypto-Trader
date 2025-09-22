// currencies.component.ts 
import {Component, OnInit} from "@angular/core";
import {
    GetAllCurrenciesService
} from "../../../services/net/http/currency/get-all-currencies.service";
import {
    CurrencyList,
    DisplayCurrencyList
} from "../../../models/currency/types";

@Component({
    selector: 'currencies',
    standalone: false,
    templateUrl: './currencies.component.html',
    styleUrls: ['./currencies.component.scss']
})
export class CurrenciesComponent implements OnInit {
    currencies: DisplayCurrencyList = { currencies: [] };
    constructor(private getAllCurrenciesService: GetAllCurrenciesService) {
        
    }
    
    onError(event: Event) {
        if (event instanceof ErrorEvent) {
            console.log(event.message);
        }
    }
    
    ngOnInit(): void {
        this.listenForCurrencies();
    }

    listenForCurrencies(): void {
        this.getAllCurrenciesService.getAllCurrencies().subscribe((data: DisplayCurrencyList) => {
            console.log(data);
            this.currencies = data;
            this.currencies.currencies = this.currencies.currencies.slice(0, 10);
        });
    }
}

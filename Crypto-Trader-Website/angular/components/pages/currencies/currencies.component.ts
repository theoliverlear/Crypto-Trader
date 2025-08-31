// currencies.component.ts 
import {Component, OnInit} from "@angular/core";
import {
    GetAllCurrenciesService
} from "../../../services/net/http/get-all-currencies.service";
import {CurrencyList} from "../../../models/currency/types";

@Component({
    selector: 'currencies',
    templateUrl: './currencies.component.html',
    styleUrls: ['./currencies.component.scss']
})
export class CurrenciesComponent implements OnInit {
    constructor(private getAllCurrenciesService: GetAllCurrenciesService) {
        
    }
    
    ngOnInit(): void {
        this.listenForCurrencies();
    }

    listenForCurrencies() {
        this.getAllCurrenciesService.getAllCurrencies().subscribe((data: CurrencyList) => {
            console.log(data);
        });
    }
}

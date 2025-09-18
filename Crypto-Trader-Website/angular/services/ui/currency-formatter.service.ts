import {Injectable} from "@angular/core";

@Injectable({
    providedIn: 'root'
})
export class CurrencyFormatterService {
    constructor() {

    }
    
    public formatCurrency(amount: number): string {
        let numDigits: number = 2;
        if (amount < 1) {
            const decimalSplit: string = amount.toString().split('.')[1];
            numDigits = decimalSplit.length;
        }
        const formatter: Intl.NumberFormat = new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'USD',
            minimumFractionDigits: numDigits
        });
        return formatter.format(amount);
    }
}
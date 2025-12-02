import {Injectable} from "@angular/core";

@Injectable({
    providedIn: 'root'
})
export class SharesFormatterService {
    constructor() {
        
    }
    
    public formatShares(shares: number, currencyCode: string): string {
        let numDecimals: number = 2;
        if (shares < 1) {
            let sharesString: string = shares.toString().split(".")[1];
            if (sharesString !== undefined) {
                numDecimals = sharesString.length;
                for (let i = 0; i < numDecimals; i++) {
                    if (sharesString[i] !== "0") {
                        numDecimals = i + 1;
                        break;
                    }
                }
            }
        }
        const sharesFormatter = new Intl.NumberFormat('en-US', {
            style: 'decimal',
            minimumFractionDigits: 0,
            maximumFractionDigits: numDecimals
        });
        const sharesChange: number = sharesFormatter.format(shares) as unknown as number;
        return sharesChange.toString() + " " + currencyCode.toUpperCase();
    }
}
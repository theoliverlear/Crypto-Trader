// trade-console.component.ts
import {Component, OnInit} from "@angular/core";
import {PossibleVendor} from "../../../../models/vendor/types";
import {VendorOption} from "../../../../models/vendor/VendorOption";
import {TradeConsoleTitle} from "./models/types";
import {TradeConsoleTitles} from "./models/TradeConsoleTitles";
import {TagType} from "@theoliverlear/angular-suite";
import {
    CurrencyNamesService
} from "../../../../services/net/http/currency/currency-names.service";
import {CurrencyNames} from "../../../../models/currency/types";

@Component({
    selector: 'trade-console',
    templateUrl: './trade-console.component.html',
    styleUrls: ['./trade-console.component.scss'],
    standalone: false
})
export class TradeConsoleComponent implements OnInit {
    protected currentTitle: TradeConsoleTitle = TradeConsoleTitles.VENDOR;
    protected selectedVendor: PossibleVendor = null;
    protected isVendorSelected: boolean = false;
    protected isCurrencySelected: boolean = false;
    protected currencyNames: string[] = [];
    constructor(private currencyNamesService: CurrencyNamesService) {
        
    }
    
    ngOnInit(): void {
        this.currencyNamesService.getCurrencyNames(true).subscribe({
            next: (currencyNames: CurrencyNames) => {
                console.log(currencyNames);
                this.currencyNames = currencyNames.currencyNames;
                this.sortCurrencyNamesAlphabetically();
            }
        })
    }
    
    private sortCurrencyNamesAlphabetically(): void {
        this.currencyNames = [...this.currencyNames].sort((initialString, compareString) =>
            initialString.localeCompare(compareString, undefined, { 
                sensitivity: 'base' 
            })
        );
    }
    
    shouldShowCurrencySearch(): boolean {
        const isEmpty: boolean = this.currencyNames.length === 0;
        return !isEmpty;
    }
    
    setVendorOption(vendorOption: VendorOption): void {
        this.selectedVendor = vendorOption;
        this.isVendorSelected = true;
        this.currentTitle = TradeConsoleTitles.CURRENCY;
    }

    protected readonly TagType = TagType;
}
// trade-console.component.ts
import { Component, OnInit } from '@angular/core';

import { TagType } from '@theoliverlear/angular-suite';
import { CurrencyNamesService } from '@http/currency/currency-names.service';
import { DisplayCurrencyService } from '@http/currency/display-currency.service';
import {
    CurrencyNames,
    DisplayCurrency,
    PossibleDisplayCurrency,
} from '@models/currency/types';
import { PossibleVendor } from '@models/vendor/types';
import { VendorOption } from '@models/vendor/VendorOption';

import { TradeConsoleTitles } from './models/TradeConsoleTitles';
import { TradeConsoleTitle } from './models/types';

@Component({
    selector: 'trade-console',
    templateUrl: './trade-console.component.html',
    styleUrls: ['./trade-console.component.scss'],
    standalone: false,
})
export class TradeConsoleComponent implements OnInit {
    protected currentTitle: TradeConsoleTitle = TradeConsoleTitles.VENDOR;
    protected selectedVendor: PossibleVendor = null;
    protected isVendorSelected: boolean = false;
    protected isCurrencySelected: boolean = false;
    protected currencyNames: string[] = [];
    private _selectedCurrency: string;
    protected displayCurrency: PossibleDisplayCurrency = null;
    constructor(
        private currencyNamesService: CurrencyNamesService,
        private displayCurrencyService: DisplayCurrencyService,
    ) {}

    protected set selectedCurrency(value: string) {
        this._selectedCurrency = value;
        if (value) {
            const code: string | undefined = this.getCurrencyCode();
            if (code) {
                this.displayCurrencyService.getDisplayCurrency(code).subscribe({
                    next: (displayCurrency: DisplayCurrency): void => {
                        this.displayCurrency = displayCurrency;
                    },
                });
            }
        }
    }

    updateCurrency(newCurrency: string): void {
        this.selectedCurrency = newCurrency;
        this.isCurrencySelected = true;
    }

    getCurrencyCode(): string | undefined {
        const currencyCode: string | undefined =
            this._selectedCurrency?.match(/.*[(](.*)[)]/)?.[1];
        return currencyCode;
    }

    ngOnInit(): void {
        this.currencyNamesService.getCurrencyNames(true).subscribe({
            next: (currencyNames: CurrencyNames): void => {
                this.currencyNames = currencyNames.currencyNames;
                this.sortCurrencyNamesAlphabetically();
            },
        });
    }

    private sortCurrencyNamesAlphabetically(): void {
        this.currencyNames = [...this.currencyNames].sort(
            (initialString, compareString): number =>
                initialString.localeCompare(compareString, undefined, {
                    sensitivity: 'base',
                }),
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

    clearContainer(): void {
        this.selectedVendor = null;
        this.isVendorSelected = false;
        this.isCurrencySelected = false;
        this.displayCurrency = null;
    }

    protected readonly TagType = TagType;
}

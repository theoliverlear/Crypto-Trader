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
import { CryptoTraderLoggerService } from '@services/logging/crypto-trader-logger.service';

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
        private readonly logger: CryptoTraderLoggerService,
    ) {}

    protected set selectedCurrency(value: string) {
        this.logger.debug(`Currency selected: ${value}`, 'TradeConsole');
        this._selectedCurrency = value;
        if (value) {
            const code: string | undefined = this.getCurrencyCode();
            if (code) {
                this.logger.debug(`Fetching display currency for code: ${code}`, 'TradeConsole');
                this.displayCurrencyService.getDisplayCurrency(code).subscribe({
                    next: (displayCurrency: DisplayCurrency): void => {
                        this.logger.info(`Display currency loaded: ${displayCurrency.currencyName}`, 'TradeConsole');
                        this.displayCurrency = displayCurrency;
                    },
                    error: (error): void => {
                        this.logger.error(`Failed to load display currency for ${code}`, error, 'TradeConsole');
                    },
                });
            } else {
                this.logger.warn(`Could not extract currency code from: ${value}`, 'TradeConsole');
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
        this.logger.info('TradeConsoleComponent initialized', 'TradeConsole');
        this.currencyNamesService.getCurrencyNames(true).subscribe({
            next: (currencyNames: CurrencyNames): void => {
                this.logger.info(`Loaded ${currencyNames.currencyNames.length} currency names`, 'TradeConsole');
                this.currencyNames = currencyNames.currencyNames;
                this.sortCurrencyNamesAlphabetically();
            },
            error: (error): void => {
                this.logger.error('Failed to load currency names', error, 'TradeConsole');
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
        this.logger.info(`Vendor selected: ${vendorOption}`, 'TradeConsole');
        this.selectedVendor = vendorOption;
        this.isVendorSelected = true;
        this.currentTitle = TradeConsoleTitles.CURRENCY;
    }

    clearContainer(): void {
        this.logger.debug('Clearing trade console container', 'TradeConsole');
        this.selectedVendor = null;
        this.isVendorSelected = false;
        this.isCurrencySelected = false;
        this.displayCurrency = null;
    }

    protected readonly TagType = TagType;
}

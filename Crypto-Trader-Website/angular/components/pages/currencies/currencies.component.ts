// currencies.component.ts 
import {Component, HostListener, OnInit} from "@angular/core";
import { animate, query, stagger, style, transition, trigger } from "@angular/animations";
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
    styleUrls: ['./currencies.component.scss'],
    animations: [
        trigger('listStagger', [
            transition('* => *', [
                query(':enter', [
                    style({ opacity: 0, transform: 'translateY(8px)' }),
                    stagger(800, [
                        animate('600ms ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
                    ])
                ], { optional: true })
            ])
        ])
    ]
})
export class CurrenciesComponent implements OnInit {
    currencies: DisplayCurrencyList = { currencies: [] };
    currencyIds: number[] = [];
    private loadingStatus: Record<number, boolean> = {};
    isLoaded: boolean = false;
    isFetching: boolean = false;
    loadingNewItems: boolean = false;
    private offset: number = 0;
    private readonly pageSize: number = 10;
    hasMore: boolean = true;
    private scrollTimer: any = null;
    constructor(private getAllCurrenciesService: GetAllCurrenciesService) {
        
    }
    
    @HostListener('window:scroll', [])
    onScroll() {
        if (this.scrollTimer) {
            clearTimeout(this.scrollTimer);
        }
        this.scrollTimer = setTimeout(() => {
            this.scrollTimer = null;
            if (this.loadingNewItems || this.isFetching || !this.hasMore) {
                return;
            }
            const scrollPosition: number = window.scrollY + window.innerHeight;
            const pageHeight: number = document.documentElement.scrollHeight;
            if (this.shouldLoadMoreCurrencies(scrollPosition, pageHeight)) {
                this.triggerLoadMoreCurrencies();
            }
        }, 150);
    }
    
    onError(event: Event) {
        if (event instanceof ErrorEvent) {
            console.log(event.message);
        }
    }
    
    ngOnInit(): void {
        this.initializeCurrencies();
        this.loadCurrencies(0);
    }

    listenForCurrencies(): void {
        this.getAllCurrenciesService.getAllCurrenciesWithOffset(0).subscribe((data: DisplayCurrencyList) => {
            this.currencies = data;
        });
    }

    private loadCurrencies(offset: number): void {
        this.isFetching = true;
        this.getAllCurrenciesService.getAllCurrenciesWithOffset(offset).subscribe({
            next: (data: DisplayCurrencyList) => {
                // append results
                this.currencies.currencies = [
                    ...(this.currencies.currencies || []),
                    ...(data?.currencies || [])
                ];
                const received: number = data?.currencies?.length || 0;
                if (received === this.pageSize) {
                    this.offset += this.pageSize;
                } else {
                    // no more data to load
                    this.hasMore = false;
                }
                this.isLoaded = true;
                this.isFetching = false;
            },
            error: () => {
                this.isFetching = false;
            }
        });
    }

    shouldLoadMoreCurrencies(scrollPosition: number, pageHeight: number): boolean {
        return scrollPosition >= pageHeight - 100;
    }

    private triggerLoadMoreCurrencies(): void {
        if (this.isFetching) {
            return;
        }
        this.isFetching = true;
        this.loadCurrencies(this.offset);
    }

    private loadMoreCurrencies(): void {
        this.loadingNewItems = true;
        setTimeout((): void => {
            let nextCurrencyId: number;
            if (this.currencies.currencies.length > 0) {
                nextCurrencyId = Math.max(...this.currencyIds) + 1;
            } else {
                nextCurrencyId = 0;
            }
            for (let i: number = 0; i < 5; i++) {
                const currencyId: number = nextCurrencyId + i;
                this.currencyIds.push(currencyId);
                this.loadingStatus[currencyId] = false;
            }
            this.loadingNewItems = false;
        }, 1000);
    }

    onCurrencyLoaded(paycheckId: number): void {
        if (this.loadingStatus.hasOwnProperty(paycheckId)) {
            this.loadingStatus[paycheckId] = true;
        }
        if (this.allPaychecksLoaded() && this.isFetching) {
            this.isFetching = false;
        }
        if (!this.isLoaded && this.initialCurrenciesLoaded()) {
            this.isLoaded = true;
        }
    }

    private allPaychecksLoaded(): boolean {
        return Object.values(this.loadingStatus).every((value: boolean): boolean => value);
    }

    private initialCurrenciesLoaded(): boolean {
        const initialIds: number[] = this.currencyIds.slice(0, 10);
        return initialIds.every((id: number): boolean => this.loadingStatus[id]);
    }

    private initializeCurrencies(): void {
        for (let i: number = 0; i < 10; i++) {
            this.currencyIds.push(i);
            this.loadingStatus[i] = false;
        }
    }
}

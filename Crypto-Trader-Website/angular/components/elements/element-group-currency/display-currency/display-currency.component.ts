// display-currency.component.ts
import {
    AfterViewInit,
    Component,
    Input,
    OnChanges,
    SimpleChanges
} from "@angular/core";
import {
    DisplayCurrency,
    HistoryPoint
} from "../../../../models/currency/types";
import {
    defaultCurrencyIcon,
    ImageAsset
} from "../../../../assets/imageAssets";
import {ElementSize, TagType} from "@theoliverlear/angular-suite";
import {
    CurrencyFormatterService
} from "../../../../services/currency-formatter.service";
import {
    GetCurrencyHistoryService
} from "../../../../services/net/http/get-currency-history.service";

@Component({
    selector: 'display-currency',
    standalone: false,
    templateUrl: './display-currency.component.html',
    styleUrls: ['./display-currency.component.scss']
})
export class DisplayCurrencyComponent implements OnChanges, AfterViewInit {
    @Input() currency: DisplayCurrency;
    imageAsset: ImageAsset = defaultCurrencyIcon;
    history: HistoryPoint[] = [];

    constructor(private currencyFormatter: CurrencyFormatterService,
                private historyService: GetCurrencyHistoryService) {}

    getCurrencyPrice(): string {
        if (!this.currency) {
            return this.currencyFormatter.formatCurrency(0);
        } else {
            return this.currencyFormatter.formatCurrency(this.currency.value);
        }
    }
    
    // TODO: If currency is down for the day, show a down arrow in red. If it
    //       is up for the day, show an up arrow in green.

    ngOnChanges(changes: SimpleChanges) {
        if (changes['currency']) {
            this.resolveImageAsset();
            this.fetchHistory();
        }
    }

    ngAfterViewInit() {
        this.resolveImageAsset();
    }

    fetchHistory(): void {
        if (!this.isCurrencyInvalid()) {
            this.listenForCurrencyHistory();
        }
    }

    private listenForCurrencyHistory() {
        this.historyService.getHistory(this.currency.currencyCode, 24, 60)
            .subscribe(points => {
                this.history = points;
            });
    }

    private isCurrencyInvalid() {
        return !this.currency || !this.currency.currencyCode;
    }

    async resolveImageAsset(): Promise<void> {
        if (!this.currency) { return; }
        const imageAsset: ImageAsset = {
            src: this.currency.logoUrl,
            alt: this.currency.currencyName + " logo",
        };
        const imageLoads: boolean = await this.imageLoads(imageAsset.src);
        if (imageLoads) {
            this.imageAsset = imageAsset;
        } else {
            this.imageAsset = defaultCurrencyIcon;
        }
    }

    async imageLoads(src: string): Promise<boolean> {
        return new Promise<boolean>((resolve) => {
            const image = new Image();
            image.onload = () => {
                resolve(true);
            }
            image.onerror = () => {
                resolve(false);
            }
            image.src = src;
        });
    }

    protected readonly TagType = TagType;
    protected readonly ElementSize = ElementSize;
}
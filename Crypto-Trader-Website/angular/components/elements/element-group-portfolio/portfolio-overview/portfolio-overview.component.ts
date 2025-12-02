// portfolio-overview.component.ts
import {
    Component,
    Input,
    OnChanges,
    OnInit,
    SimpleChanges
} from "@angular/core";
import {Portfolio} from "../../../../models/portfolio/types";
import {TagType} from "@theoliverlear/angular-suite";
import {
    TimeFormatterService
} from "../../../../services/ui/time-formatter.service";
import {
    CurrencyFormatterService
} from "../../../../services/ui/currency-formatter.service";
import {ImageAsset} from "../../../../assets/imageAssets";
import {
    CurrencyImageService
} from "../../../../services/ui/currency-image.service";

@Component({
    selector: 'portfolio-overview',
    templateUrl: './portfolio-overview.component.html',
    styleUrls: ['./portfolio-overview.component.scss'],
    standalone: false
})
export class PortfolioOverviewComponent implements OnInit, OnChanges {
    @Input() portfolio: Portfolio;
    currencyImagesValues: [string, ImageAsset][] = [];
    constructor(private currencyFormatter: CurrencyFormatterService,
                private timeFormatter: TimeFormatterService,
                private currencyImageService: CurrencyImageService) {
        
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes['portfolio']) {
            this.loadCurrencyImages();
        }
    }

    ngOnInit(): void {
        this.loadCurrencyImages();
    }
    
    hasCurrencies() {
        return this.portfolio.assets.length > 0;
    }
    
    async loadCurrencyImages(): Promise<void> {
        this.currencyImagesValues = await Promise.all(this.portfolio.assets.map(async asset => {
            const currencyImage: ImageAsset = await this.currencyImageService.resolveImageAsset(asset.currencyCode);
            const assetValue: string = this.currencyFormatter.formatCurrency(asset.totalValueInDollars);
            return [assetValue, currencyImage] as [string, ImageAsset];
        }).reverse());
    }
    
    getDollarBalance(): string {
        return this.currencyFormatter.formatCurrency(this.portfolio.dollarBalance);
    }
    
    getSharesBalance(): string {
        return this.currencyFormatter.formatCurrency(this.portfolio.sharesBalance);
    }
    
    getTotalWorth(): string {
        return this.currencyFormatter.formatCurrency(this.portfolio.totalWorth);
    }
    
    getLastUpdated(): string {
        return this.timeFormatter.formatTime(this.portfolio.lastUpdated);
    }

    protected readonly TagType = TagType;
}
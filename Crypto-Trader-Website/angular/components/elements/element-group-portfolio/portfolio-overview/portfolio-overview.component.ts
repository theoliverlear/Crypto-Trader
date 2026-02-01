// portfolio-overview.component.ts
import {
    Component,
    Input,
    OnChanges,
    OnInit,
    SimpleChanges,
} from '@angular/core';

import { TagType } from '@theoliverlear/angular-suite';
import { ImageAsset } from '@assets/imageAssets';
import { CurrencyFormatterService } from '@ui/currency-formatter.service';
import { CurrencyImageService } from '@ui/currency-image.service';
import { SharesFormatterService } from '@ui/shares-formatter.service';
import { TimeFormatterService } from '@ui/time-formatter.service';
import { Portfolio, PortfolioAsset } from '@models/portfolio/types';

@Component({
    selector: 'portfolio-overview',
    templateUrl: './portfolio-overview.component.html',
    styleUrls: ['./portfolio-overview.component.scss'],
    standalone: false,
})
export class PortfolioOverviewComponent implements OnInit, OnChanges {
    @Input() portfolio: Portfolio;
    currencyImagesValues: [string, ImageAsset][] = [];
    constructor(
        private currencyFormatter: CurrencyFormatterService,
        private timeFormatter: TimeFormatterService,
        private currencyImageService: CurrencyImageService,
        private sharesFormatter: SharesFormatterService,
    ) {}

    ngOnChanges(changes: SimpleChanges): void {
        if (changes['portfolio']) {
            void this.loadCurrencyImages();
        }
    }

    ngOnInit(): void {
        void this.loadCurrencyImages();
    }

    hasCurrencies(): boolean {
        return this.portfolio.assets.length > 0;
    }

    async loadCurrencyImages(): Promise<void> {
        this.currencyImagesValues = await Promise.all(
            this.portfolio.assets
                .map(async (asset: PortfolioAsset) => {
                    const currencyImage: ImageAsset =
                        await this.currencyImageService.resolveImageAsset(
                            asset.currencyCode,
                        );
                    const assetValue: string =
                        this.currencyFormatter.formatCurrency(
                            asset.totalValueInDollars,
                        );
                    return [assetValue, currencyImage] as [string, ImageAsset];
                })
                .reverse(),
        );
    }

    getDollarBalance(): string {
        return this.currencyFormatter.formatCurrency(
            this.portfolio.dollarBalance,
        );
    }

    getSharesBalance(): string {
        return this.sharesFormatter.formatShares(
            this.portfolio.shareBalance,
            'SHARES',
        );
    }

    getTotalWorth(): string {
        return this.currencyFormatter.formatCurrency(this.portfolio.totalWorth);
    }

    getLastUpdated(): string {
        return this.timeFormatter.formatTime(this.portfolio.lastUpdated);
    }

    protected readonly TagType = TagType;
}

// portfolio-asset-list.component.ts
import {Component, Input, OnInit} from "@angular/core";
import {PortfolioAsset} from "../../../../models/portfolio/types";
import {AssetFieldSortType} from "../../../../models/sort/types";
import {SimpleSortState} from "../../../../models/sort/SimpleSortState";
import {
    PortfolioAssetFieldType
} from "../asset-field/models/PortfolioAssetFieldType";

@Component({
    selector: 'portfolio-asset-list',
    templateUrl: './portfolio-asset-list.component.html',
    styleUrls: ['./portfolio-asset-list.component.scss'],
    standalone: false
})
export class PortfolioAssetListComponent implements OnInit {
    @Input() assets: PortfolioAsset[] = [];
    originalAssetsState: PortfolioAsset[] = [];
    sortState: AssetFieldSortType = [PortfolioAssetFieldType.CURRENCY_NAME, SimpleSortState.NONE];
    constructor() {
        
    }
    
    ngOnInit(): void {
        this.attemptStoreOriginalAssets();
    }

    private attemptStoreOriginalAssets() {
        if (this.hasAssets()) {
            this.originalAssetsState = [...this.assets];
        }
    }

    hasAssets(): boolean {
        return this.assets.length > 0;
    }
    
    sortAssets(assetSortType: AssetFieldSortType): void {
        if (!this.canSort()) {
            return;
        }
        this.sortState = assetSortType;
        if (assetSortType[1] === SimpleSortState.NONE) {
            this.resetSort();
            return;
        }
        this.sortByType(assetSortType);
    }

    private sortByType(assetSortType: AssetFieldSortType) {
        switch (assetSortType[0]) {
            case PortfolioAssetFieldType.TOTAL_VALUE:
                this.sortByTotalValue();
                break;
            case PortfolioAssetFieldType.CURRENCY_NAME:
                this.sortByCurrencyName();
                break;
            case PortfolioAssetFieldType.SHARES:
                this.sortByShares();
                break;
            case PortfolioAssetFieldType.LAST_UPDATED:
                this.sortByLastUpdated();
                break;
            case PortfolioAssetFieldType.TARGET_PRICE:
                this.sortByTargetPrice();
                break;
            case PortfolioAssetFieldType.VENDOR_NAME:
                this.sortByVendorName();
                break;
            default:
                throw new Error("Unknown AssetFieldSortType: " + assetSortType);
        }
    }

    canSort(): boolean {
        return this.assets.length > 1;
    }
    
    sortByCurrencyName(): void {
        const sortType: SimpleSortState = this.sortState[1];
        if (sortType === SimpleSortState.ASCENDING) {
            this.assets.sort((a, b) => a.currencyName.localeCompare(b.currencyName));
        } else {
            this.assets.sort((a, b) => b.currencyName.localeCompare(a.currencyName));
        }
    }
    
    sortByShares(): void {
        const sortType: SimpleSortState = this.sortState[1];
        if (sortType === SimpleSortState.ASCENDING) {
            this.assets.sort((a, b) => a.shares - b.shares);
        } else {
            this.assets.sort((a, b) => b.shares - a.shares);
        }
    }
    
    sortByTotalValue(): void {
        const sortType: SimpleSortState = this.sortState[1];
        if (sortType === SimpleSortState.ASCENDING) {
            this.assets.sort((a, b) => a.totalValueInDollars - b.totalValueInDollars);
        } else {
            this.assets.sort((a, b) => b.totalValueInDollars - a.totalValueInDollars);
        }       
    }
    
    sortByTargetPrice(): void {
        const sortType: SimpleSortState = this.sortState[1];
        if (sortType === SimpleSortState.ASCENDING) {
            this.assets.sort((a, b) => a.targetPrice - b.targetPrice);
        } else {
            this.assets.sort((a, b) => b.targetPrice - a.targetPrice);
        }       
    }
    
    sortByVendorName(): void {
        const sortType: SimpleSortState = this.sortState[1];
        if (sortType === SimpleSortState.ASCENDING) {
            this.assets.sort((a, b) => a.vendorName.localeCompare(b.vendorName));
        } else {
            this.assets.sort((a, b) => b.vendorName.localeCompare(a.vendorName));
        }       
    }
    
    sortByLastUpdated(): void {
        const sortType: SimpleSortState = this.sortState[1];
        if (sortType === SimpleSortState.ASCENDING) {
            this.assets.sort((a, b) => a.lastUpdated.localeCompare(b.lastUpdated));
        } else {
            this.assets.sort((a, b) => b.lastUpdated.localeCompare(a.lastUpdated));
        }
    }
    
    resetSort(): void {
        this.assets = [...this.originalAssetsState];
    }
}
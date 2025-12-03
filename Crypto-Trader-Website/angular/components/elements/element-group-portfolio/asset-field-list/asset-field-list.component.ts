// asset-field-list.component.ts
import {Component, Input} from "@angular/core";
import {PortfolioAsset} from "../../../../models/portfolio/types";
import {
    PortfolioAssetFieldType
} from "../asset-field/models/PortfolioAssetFieldType";

@Component({
    selector: 'asset-field-list',
    templateUrl: './asset-field-list.component.html',
    styleUrls: ['./asset-field-list.component.scss'],
    standalone: false
})
export class AssetFieldListComponent {
    // TODO: Put repeated default PortfolioAsset in assets file.
    @Input() asset: PortfolioAsset = {
        id: 0,
        currencyName: "",
        currencyCode: "",
        shares: 0,
        sharesValueInDollars: 0,
        assetWalletDollars: 0,
        totalValueInDollars: 0,
        targetPrice: 0,
        lastUpdated: "",
        vendorName: ""
    };
    @Input() showFieldTitles: boolean = true;
    constructor() {
        
    }

    protected readonly PortfolioAssetFieldType = PortfolioAssetFieldType;
}
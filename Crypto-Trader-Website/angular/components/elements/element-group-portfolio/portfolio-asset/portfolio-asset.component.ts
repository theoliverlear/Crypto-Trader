// portfolio-asset.component.ts
import { Component, Input } from "@angular/core";
import {PortfolioAsset} from "../../../../models/portfolio/types";

@Component({
    selector: 'portfolio-asset',
    templateUrl: './portfolio-asset.component.html',
    styleUrls: ['./portfolio-asset.component.scss'],
    standalone: false
})
export class PortfolioAssetComponent {
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
    @Input() isFirst: boolean = false;
    constructor() {
        
    }
}
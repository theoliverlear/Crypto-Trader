// portfolio-asset-list.component.ts
import {Component, Input} from "@angular/core";
import {PortfolioAsset} from "../../../../models/portfolio/types";

@Component({
    selector: 'portfolio-asset-list',
    templateUrl: './portfolio-asset-list.component.html',
    styleUrls: ['./portfolio-asset-list.component.scss'],
    standalone: false
})
export class PortfolioAssetListComponent {
    @Input() assets: PortfolioAsset[] = [];
    constructor() {
        
    }
    
    hasAssets(): boolean {
        return this.assets.length > 0;
    }
}
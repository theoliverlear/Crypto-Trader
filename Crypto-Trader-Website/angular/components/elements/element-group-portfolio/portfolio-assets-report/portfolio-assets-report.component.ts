// portfolio-assets-report.component.ts
import {Component, Input} from "@angular/core";
import {Portfolio, PortfolioAsset} from "../../../../models/portfolio/types";
import {TagType} from "@theoliverlear/angular-suite";

@Component({
    selector: 'portfolio-assets-report',
    templateUrl: './portfolio-assets-report.component.html',
    styleUrls: ['./portfolio-assets-report.component.scss'],
    standalone: false
})
export class PortfolioAssetsReportComponent {
    @Input() portfolio: Portfolio;
    constructor() {
        
    }

    hasCurrencies() {
        return this.portfolio.assets.length > 0;
    }

    protected readonly TagType = TagType;
}
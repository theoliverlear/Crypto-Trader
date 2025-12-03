import {Component, OnInit} from "@angular/core";
import {TagType} from "@theoliverlear/angular-suite";
import {
    PortfolioService
} from "../../../services/net/http/portfolio/portfolio.service";
import {Portfolio} from "../../../models/portfolio/types";

@Component({
    selector: 'portfolio',
    standalone: false,
    templateUrl: './portfolio.component.html',
    styleUrls: ['./portfolio.component.scss']
})
export class PortfolioComponent implements OnInit {
    portfolio: Portfolio = {
        assets: [],
        dollarBalance: 0,
        lastUpdated: "",
        sharesBalance: 0,
        totalWorth: 0
    };
    constructor(private portfolioService: PortfolioService) {

    }
    
    ngOnInit(): void {
        this.portfolioService.getPortfolio().subscribe((data: Portfolio) => {
            this.portfolio = data;
        });
    }
    
    hasCurrencies() {
        return this.portfolio.assets.length > 0;
    }
    
    protected readonly TagType = TagType;
}
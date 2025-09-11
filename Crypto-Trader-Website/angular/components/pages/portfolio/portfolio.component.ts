import {Component} from "@angular/core";
import {TagType} from "@theoliverlear/angular-suite";

@Component({
    selector: 'portfolio',
    standalone: false,
    templateUrl: './portfolio.component.html',
    styleUrls: ['./portfolio.component.scss']
})
export class PortfolioComponent {
    constructor() {

    }

    protected readonly TagType = TagType;
}
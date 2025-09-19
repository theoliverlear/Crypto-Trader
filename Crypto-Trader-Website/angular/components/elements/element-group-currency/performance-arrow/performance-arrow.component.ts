// performance-arrow.component.ts
import {Component, HostBinding, Input} from "@angular/core";
import {
    CurrencyPerformanceRating,
    PerformanceRating
} from "../../../../models/currency/types";
import {upArrowIcon} from "../../../../assets/imageAssets";
import {TagType} from "@theoliverlear/angular-suite";

@Component({
    selector: 'performance-arrow',
    templateUrl: './performance-arrow.component.html',
    styleUrls: ['./performance-arrow.component.scss'],
    standalone: false
})
export class PerformanceArrowComponent {
    @Input() performance: PerformanceRating = { rating: 'neutral', changePercent: '0%' };
    @Input() includePercent: boolean = false;
    @HostBinding('class.up') get isUp() {
        return this.performance && this.performance.rating === "up";
    }
    @HostBinding('class.down') get isDown() {
        return this.performance && this.performance.rating === "down";
    }
    constructor() {
        
    }

    protected readonly upArrowIcon = upArrowIcon;
    protected readonly TagType = TagType;
}
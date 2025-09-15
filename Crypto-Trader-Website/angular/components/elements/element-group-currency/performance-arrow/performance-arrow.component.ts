// performance-arrow.component.ts
import {Component, HostBinding, Input} from "@angular/core";
import {PerformanceRating} from "../../../../models/currency/types";
import {upArrowIcon} from "../../../../assets/imageAssets";

@Component({
    selector: 'performance-arrow',
    templateUrl: './performance-arrow.component.html',
    styleUrls: ['./performance-arrow.component.scss'],
    standalone: false
})
export class PerformanceArrowComponent {
    @Input() performance: PerformanceRating;
    @HostBinding('class.up') get isUp() {
        return this.performance === "up";
    }
    @HostBinding('class.down') get isDown() {
        return this.performance === "down";
    }
    constructor() {
        
    }

    protected readonly upArrowIcon = upArrowIcon;
}
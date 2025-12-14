// portfolio-section-arrow.component.ts
import {Component, Input} from "@angular/core";
import {ArrowDirection} from "../../arrow/models/ArrowDirection";
import {PortfolioSectionArrowType} from "./models/PortfolioSectionArrowType";

@Component({
    selector: 'portfolio-section-arrow',
    templateUrl: './portfolio-section-arrow.component.html',
    styleUrls: ['./portfolio-section-arrow.component.scss'],
    standalone: false
})
export class PortfolioSectionArrowComponent {
    @Input() sectionType: PortfolioSectionArrowType;
    constructor() {
        
    }
    
    getArrowDirection(): ArrowDirection {
        switch (this.sectionType) {
            case PortfolioSectionArrowType.REPORT:
                return ArrowDirection.LEFT;
            case PortfolioSectionArrowType.MANAGE:
                return ArrowDirection.RIGHT;
            default:
                return ArrowDirection.RIGHT;
        }
    }
}
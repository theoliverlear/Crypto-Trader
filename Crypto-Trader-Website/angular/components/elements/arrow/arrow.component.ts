// arrow.component.ts
import {Component, Input} from "@angular/core";
import {ArrowDirection} from "./models/ArrowDirection";

@Component({
    selector: 'arrow',
    templateUrl: './arrow.component.html',
    styleUrls: ['./arrow.component.scss'],
    standalone: false
})
export class ArrowComponent {
    @Input() direction: ArrowDirection = ArrowDirection.RIGHT;
    @Input() text: string | undefined
    constructor() {
        
    }

    get classes(): string {
        return this.direction;
    }
}
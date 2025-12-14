// arrow.component.ts
import {Component, HostBinding, Input} from "@angular/core";
import {ArrowDirection} from "./models/ArrowDirection";
import {TagType} from "@theoliverlear/angular-suite";

@Component({
    selector: 'arrow',
    templateUrl: './arrow.component.html',
    styleUrls: ['./arrow.component.scss'],
    standalone: false
})
export class ArrowComponent {
    @Input() direction: ArrowDirection = ArrowDirection.RIGHT;
    @Input() text: string | undefined
    @HostBinding('class')
    get hostClasses(): string {
        return this.direction;
    }
    constructor() {
        
    }

    protected readonly TagType = TagType;
}
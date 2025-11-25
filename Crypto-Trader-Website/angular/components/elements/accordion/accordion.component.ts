// accordion.component.ts
import {Component, Input} from "@angular/core";

@Component({
    selector: 'accordion',
    templateUrl: './accordion.component.html',
    styleUrls: ['./accordion.component.scss'],
    standalone: false
})
export class AccordionComponent {
    @Input() title: string = "";
    @Input() description: string = "";
    constructor() {
        
    }
}
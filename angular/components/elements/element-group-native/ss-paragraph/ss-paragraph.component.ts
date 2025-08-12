// ss-paragraph.component.ts
import { Component, Input } from "@angular/core";

@Component({
    selector: 'ss-paragraph',
    templateUrl: './ss-paragraph.component.html',
    styleUrls: ['./ss-paragraph.component.css']
})
export class SsParagraphComponent {
    @Input() text: string;
    constructor() {
        
    }
}

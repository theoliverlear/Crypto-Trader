// ss-input.component.ts 
import {Component, EventEmitter, Output} from "@angular/core";

@Component({
    selector: 'ss-input',
    templateUrl: './ss-input.component.html',
    styleUrls: ['./ss-input-style.component.css']
})
export class SsInputComponent {
    @Output() inputEvent: EventEmitter<string> = new EventEmitter<string>();
    constructor() {

    }
}

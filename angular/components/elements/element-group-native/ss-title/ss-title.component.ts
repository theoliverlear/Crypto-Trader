// ss-title.component.ts
import {Component, Input} from "@angular/core";
import { TagType } from "../../../../models/html/TagType";

@Component({
    selector: 'ss-title',
    templateUrl: './ss-title.component.html',
    styleUrls: ['./ss-title.component.css']
})
export class SsTitleComponent {
    @Input() tagType: TagType = TagType.H1;
    @Input() text: string;
    constructor() {

    }

    protected readonly TagType = TagType;
}


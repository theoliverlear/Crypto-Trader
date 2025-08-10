// subtitle.component.ts
import {Component, Input} from "@angular/core";
import { TagType } from "../../../../models/html/TagType";

@Component({
    selector: 'subtitle',
    templateUrl: './subtitle.component.html',
    styleUrls: ['./subtitle.component.css']
})
export class SubtitleComponent {
    @Input() tagType: TagType = TagType.H2;
    @Input() text: string;
    constructor() {

    }

    protected readonly TagType = TagType;
}

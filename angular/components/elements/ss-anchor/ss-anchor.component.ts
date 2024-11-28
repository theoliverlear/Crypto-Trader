import {
    Component,
    Input, OnInit
} from "@angular/core";
import {TagType} from "../../../models/html/TagType";
import {TextElementLink} from "../../../models/link/TextElementLink";

@Component({
    selector: 'ss-anchor',
    templateUrl: './ss-anchor.component.html',
    styleUrls: ['./ss-anchor-style.component.css']
})
export class SsAnchorComponent implements OnInit {
    @Input() elementLink: TextElementLink;
    hasContent: boolean = false;
    constructor() {
        console.log('SsAnchorComponent loaded');
    }
    ngOnInit() {
        if (this.elementLink) {
            this.hasContent = !this.elementLink.hasText();
        }
    }

    protected readonly TagType = TagType;
}
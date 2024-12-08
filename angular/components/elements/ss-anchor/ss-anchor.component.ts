import {
    Component, HostListener,
    Input, OnInit
} from "@angular/core";
import {TagType} from "../../../models/html/TagType";
import {TextElementLink} from "../../../models/link/TextElementLink";
import {ElementLink} from "../../../models/link/ElementLink";
import {Router} from "@angular/router";

@Component({
    selector: 'ss-anchor',
    templateUrl: './ss-anchor.component.html',
    styleUrls: ['./ss-anchor-style.component.css']
})
export class SsAnchorComponent implements OnInit {
    @Input() elementLink: TextElementLink | ElementLink;
    hasText: boolean = false;
    constructor(private router: Router) {
        console.log('SsAnchorComponent loaded');
    }
    ngOnInit() {
        this.hasText = this.elementLink instanceof TextElementLink;
    }
    @HostListener('click')
    onClick() {
        this.router.navigate([this.elementLink.hrefLink]);
    }

    protected readonly TagType = TagType;
}
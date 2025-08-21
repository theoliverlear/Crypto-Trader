import {
    Component, HostBinding, HostListener,
    Input, OnInit
} from "@angular/core";
import {TagType} from "../../../../models/html/TagType";
import {TextElementLink} from "../../../../models/link/TextElementLink";
import {ElementLink} from "../../../../models/link/ElementLink";
import {Router} from "@angular/router";

@Component({
    selector: 'ss-anchor',
    templateUrl: './ss-anchor.component.html',
    styleUrls: ['./ss-anchor.component.scss']
})
export class SsAnchorComponent implements OnInit {
    @Input() elementLink: TextElementLink | ElementLink;
    hasText: boolean = false;
    @Input() isButton: boolean = false;
    @HostBinding('class.link-button') get isButtonClass(): boolean {
        return this.isButton;
    }

    constructor(private router: Router) {
        
    }
    ngOnInit() {
        this.hasText = this.elementLink instanceof TextElementLink;
    }
    @HostListener('click', ['$event'])
    onClick() {
        if (this.isExternalLink(this.elementLink.hrefLink)) {
            event.preventDefault();
            window.open(this.elementLink.hrefLink, this.elementLink.targetType);
        } else {
            this.router.navigate([this.elementLink.hrefLink]);
        }
    }
    isExternalLink(href: string): boolean {
        return href.startsWith('http://') ||
            href.startsWith('https://') ||
            href.startsWith('//');
    }
    protected readonly TagType = TagType;
}
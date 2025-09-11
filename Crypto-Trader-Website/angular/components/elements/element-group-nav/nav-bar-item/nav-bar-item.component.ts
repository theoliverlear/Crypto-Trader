import {
    Component,
    HostListener,
    Input,
    OnInit,
    ViewChild
} from "@angular/core";
import {
    SsAnchorComponent,
    TextElementLink
} from "@theoliverlear/angular-suite";

@Component({
    selector: 'nav-bar-item',
    standalone: false,
    templateUrl: './nav-bar-item.component.html',
    styleUrls: ['./nav-bar-item.component.scss']
})
export class NavBarItemComponent implements OnInit {
    @Input() elementLink: TextElementLink;
    @ViewChild(SsAnchorComponent) anchorComponent: SsAnchorComponent;
    constructor() {

    }
    
    ngOnInit(): void {
        if (this.elementLink) {
            this.convertTextToUpper();
        }
    }

    private convertTextToUpper() {
        this.elementLink.text = this.elementLink.text.toUpperCase();
    }

    @HostListener('click')
    onClick() {
        this.anchorComponent.onClick();
    }
}
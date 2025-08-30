import {Component, HostListener, Input, ViewChild} from "@angular/core";
import {
    SsAnchorComponent,
    TextElementLink
} from "@theoliverlear/angular-suite";

@Component({
    selector: 'nav-bar-item',
    templateUrl: './nav-bar-item.component.html',
    styleUrls: ['./nav-bar-item.component.scss']
})
export class NavBarItemComponent {
    @Input() elementLink: TextElementLink;
    @ViewChild(SsAnchorComponent) anchorComponent: SsAnchorComponent;
    constructor() {

    }
    @HostListener('click')
    onClick() {
        this.anchorComponent.onClick();
    }
}
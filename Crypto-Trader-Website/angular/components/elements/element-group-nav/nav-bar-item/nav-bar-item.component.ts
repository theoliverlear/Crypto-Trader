import {Component, HostListener, Input, ViewChild} from "@angular/core";
import {TextElementLink} from "../../../../models/link/TextElementLink";
import {SsAnchorComponent} from "../../element-group-native/ss-anchor/ss-anchor.component";

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
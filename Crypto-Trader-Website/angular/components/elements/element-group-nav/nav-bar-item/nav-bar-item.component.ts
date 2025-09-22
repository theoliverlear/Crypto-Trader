import {
    Component,
    HostListener,
    Input,
    OnInit,
    OnChanges,
    SimpleChanges,
    ViewChild
} from "@angular/core";
import {
    ElementLink,
    SsAnchorComponent,
    TextElementLink
} from "@theoliverlear/angular-suite";
import {NavBarItemOption} from "./models/NavBarItemOption";
import {ImageAsset} from "../../../../assets/imageAssets";

@Component({
    selector: 'nav-bar-item',
    standalone: false,
    templateUrl: './nav-bar-item.component.html',
    styleUrls: ['./nav-bar-item.component.scss']
})
export class NavBarItemComponent implements OnInit, OnChanges {
    elementLink: ElementLink;
    imageAsset: ImageAsset;
    @Input() navBarItemOption: NavBarItemOption;
    @ViewChild(SsAnchorComponent) anchorComponent: SsAnchorComponent;
    constructor() {

    }

    ngOnInit(): void {
        if (this.elementLink) {
            // this.convertTextToUpper();
        }
    }

    ngOnChanges(changes: SimpleChanges): void {
        if ('navBarItemOption' in changes && this.navBarItemOption) {
            this.elementLink = NavBarItemOption.getElementLink(this.navBarItemOption);
            this.imageAsset = NavBarItemOption.getImageAsset(this.navBarItemOption);
        }
    }

    private convertTextToUpper() {

    }

    @HostListener('click')
    onClick() {
        this.anchorComponent?.onClick();
    }
}
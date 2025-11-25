// page-title-stripe.component.ts
import {Component, Input} from "@angular/core";
import {TagType} from "@theoliverlear/angular-suite";

@Component({
    selector: 'page-title-stripe',
    templateUrl: './page-title-stripe.component.html',
    styleUrls: ['./page-title-stripe.component.scss'],
    standalone: false
})
export class PageTitleStripeComponent {
    @Input() pageTitle: string = "";
    @Input() pageSubtitle: string = "";
    constructor() {
        
    }

    protected readonly TagType = TagType;
}
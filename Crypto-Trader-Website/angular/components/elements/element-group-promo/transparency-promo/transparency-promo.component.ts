// transparency-promo.component.ts
import { Component } from "@angular/core";
import {transparencyParagraph} from "../../../../assets/textAssets";
import {repositoryElementLink} from "../../../../assets/elementLinkAssets";
import {TagType} from "@theoliverlear/angular-suite";

@Component({
    selector: 'transparency-promo',
    standalone: false,
    templateUrl: './transparency-promo.component.html',
    styleUrls: ['./transparency-promo.component.scss']
})
export class TransparencyPromoComponent {
    constructor() {
        
    }

    protected readonly TagType = TagType;
    protected readonly transparencyParagraph = transparencyParagraph;
    protected readonly repositoryElementLink = repositoryElementLink;
}

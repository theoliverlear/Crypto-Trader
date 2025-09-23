// vendor-option.component.ts
import {Component, Input} from "@angular/core";
import {VendorOption} from "../../../../models/vendor/VendorOption";
import {starIcon} from "../../../../assets/imageAssets";

@Component({
    selector: 'vendor-option',
    templateUrl: './vendor-option.component.html',
    styleUrls: ['./vendor-option.component.scss'],
    standalone: false
})
export class VendorOptionComponent {
    @Input() vendorOption: VendorOption;
    @Input() isRecommended: boolean = true;
    constructor() {
        
    }

    protected readonly VendorOption = VendorOption;
    protected readonly starIcon = starIcon;
}
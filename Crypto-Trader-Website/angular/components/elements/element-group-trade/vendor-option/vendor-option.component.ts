// vendor-option.component.ts
import {
    Component,
    EventEmitter,
    HostListener,
    Input,
    Output
} from "@angular/core";
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
    @Output() vendorSelectedEvent: EventEmitter<VendorOption> = new EventEmitter<VendorOption>();
    constructor() {
        
    }
    
    @HostListener('click')
    onClick() {
        this.vendorSelectedEvent.emit(this.vendorOption);
    }

    protected readonly VendorOption = VendorOption;
    protected readonly starIcon = starIcon;
}
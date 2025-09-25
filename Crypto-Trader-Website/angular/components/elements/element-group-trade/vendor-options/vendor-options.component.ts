// vendor-options.component.ts
import {Component, EventEmitter, Output} from "@angular/core";
import {VendorOption} from "../../../../models/vendor/VendorOption";
import {TagType} from "@theoliverlear/angular-suite";

@Component({
    selector: 'vendor-options',
    templateUrl: './vendor-options.component.html',
    styleUrls: ['./vendor-options.component.scss'],
    standalone: false
})
export class VendorOptionsComponent {
    @Output() vendorSelectedEvent: EventEmitter<VendorOption> = new EventEmitter<VendorOption>();
    constructor() {
        
    }

    emitVendorOption(vendorOption: VendorOption) {
        this.vendorSelectedEvent.emit(vendorOption);
    }
    
    protected readonly VendorOption = VendorOption;
    protected readonly TagType = TagType;
}
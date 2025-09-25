// trade-console.component.ts
import { Component } from "@angular/core";
import {PossibleVendor} from "../../../../models/vendor/types";
import {VendorOption} from "../../../../models/vendor/VendorOption";
import {TradeConsoleTitle} from "./models/types";
import {TradeConsoleTitles} from "./models/TradeConsoleTitles";
import {TagType} from "@theoliverlear/angular-suite";

@Component({
    selector: 'trade-console',
    templateUrl: './trade-console.component.html',
    styleUrls: ['./trade-console.component.scss'],
    standalone: false
})
export class TradeConsoleComponent {
    protected currentTitle: TradeConsoleTitle = TradeConsoleTitles.VENDOR;
    protected selectedVendor: PossibleVendor = null;
    protected isVendorSelected = false;
    constructor() {
        
    }
    setVendorOption(vendorOption: VendorOption): void {
        this.selectedVendor = vendorOption;
        this.isVendorSelected = true;
    }

    protected readonly TagType = TagType;
}
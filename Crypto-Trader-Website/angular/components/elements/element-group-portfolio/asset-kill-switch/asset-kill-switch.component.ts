// asset-kill-switch.component.ts
import {Component, HostBinding, HostListener, Input} from "@angular/core";
import {electricPlugIcon} from "../../../../assets/imageAssets";

@Component({
    selector: 'asset-kill-switch',
    templateUrl: './asset-kill-switch.component.html',
    styleUrls: ['./asset-kill-switch.component.scss'],
    standalone: false
})
export class AssetKillSwitchComponent {
    @Input() isKilled: boolean = false;
    @HostBinding('class.is-killed') get isKilledClass(): boolean { 
        return this.isKilled; 
    }
    constructor() {
        
    }

    @HostListener('click')
    onClick(): void {
        this.toggleKillSwitch();
    }
    
    toggleKillSwitch(): void {
        this.isKilled = !this.isKilled;
    }
    
    protected readonly electricPlugIcon = electricPlugIcon;
}
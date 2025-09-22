// loading-wheel.component.ts 
import {Component, HostBinding, Input} from "@angular/core";

@Component({
    selector: 'loading-wheel',
    templateUrl: './loading-wheel.component.html',
    styleUrls: ['./loading-wheel.component.css'],
    standalone: false
})
export class LoadingWheelComponent {
    @Input() visible: boolean = true;

    @HostBinding('style.visibility')
    get visibility() {
        return this.visible ? 'visible' : 'hidden';
    }

    constructor() {
        
    }

    setVisible(visible: boolean): void {
        this.visible = visible;
    }
}

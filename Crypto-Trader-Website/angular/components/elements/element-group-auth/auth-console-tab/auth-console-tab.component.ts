// auth-console-tab.component.ts 
import {
    Component,
    EventEmitter,
    HostListener,
    Input,
    Output
} from "@angular/core";
import {AuthType} from "@theoliverlear/angular-suite";

@Component({
    selector: 'auth-console-tab',
    standalone: false,
    templateUrl: './auth-console-tab.component.html',
    styleUrls: ['./auth-console-tab.component.scss']
})
export class AuthConsoleTabComponent {
    @Input() authType: AuthType;
    @Output() authTabClicked: EventEmitter<AuthType> = new EventEmitter<AuthType>();
    constructor() {
        
    }
    emitAuthTabClicked(): void {
        this.authTabClicked.emit(this.authType);
    }
    @HostListener('click')
    onClick(): void {
        this.emitAuthTabClicked();
    }
}

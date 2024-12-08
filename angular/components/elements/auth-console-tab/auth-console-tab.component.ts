// auth-console-tab.component.ts 
import {
    Component,
    EventEmitter,
    HostListener,
    Input,
    Output
} from "@angular/core";
import {AuthType} from "../../../models/auth/AuthType";

@Component({
    selector: 'auth-console-tab',
    templateUrl: './auth-console-tab.component.html',
    styleUrls: ['./auth-console-tab-style.component.css']
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

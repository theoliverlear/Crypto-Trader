// terminal.component.ts
import {
    AfterViewInit,
    Component,
    ElementRef,
    OnDestroy,
    ViewChild
} from "@angular/core";
import { CryptoTraderConsole } from "../../../../models/console/CryptoTraderConsole";
import {
    ConsoleCommandService
} from "../../../../services/net/http/console/console-command.service";
import {AuthService} from "../../../../services/net/http/auth/auth.service";
import {
    LoggedInService
} from "../../../../services/net/http/auth/status/logged-in.service";
import {
    TokenStorageService
} from "../../../../services/auth/token-storage.service";

@Component({
    selector: 'terminal',
    templateUrl: './terminal.component.html',
    styleUrls: ['./terminal.component.scss'],
    standalone: false
})
export class TerminalComponent implements AfterViewInit, OnDestroy {
    @ViewChild('host', { static: true }) host!: ElementRef<HTMLDivElement>;
    private terminal: CryptoTraderConsole;
    private resizeHandler?: () => void;
    constructor(private consoleCommandService: ConsoleCommandService,
                private authService: AuthService,
                private loggedInService: LoggedInService,
                private tokenStorageService: TokenStorageService) {
        
    }
    ngAfterViewInit(): void {
        this.terminal = new CryptoTraderConsole(this.host, 
                                                this.consoleCommandService,
                                                this.authService,
                                                this.loggedInService,
                                                this.tokenStorageService);
        this.resizeHandler = this.onResize.bind(this);
        window.addEventListener('resize', this.resizeHandler, { passive: true });
        requestAnimationFrame(() => this.terminal?.fit());
        setTimeout(() => this.terminal?.fit(), 0);
    }

    private onResize(): void {
        return this.terminal.fit();
    }
    ngOnDestroy(): void {
        if (this.resizeHandler) {
            window.removeEventListener('resize', this.resizeHandler);
            this.resizeHandler = undefined;
        }
        if (this.terminal) {
            this.terminal.dispose();
        }
    }
}
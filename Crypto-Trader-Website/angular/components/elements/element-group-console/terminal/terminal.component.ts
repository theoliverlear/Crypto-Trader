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

@Component({
    selector: 'terminal',
    templateUrl: './terminal.component.html',
    styleUrls: ['./terminal.component.scss'],
    standalone: false
})
export class TerminalComponent implements AfterViewInit, OnDestroy {
    @ViewChild('host', { static: true }) host!: ElementRef<HTMLDivElement>;
    private terminal: CryptoTraderConsole;
    constructor(private consoleCommandService: ConsoleCommandService) {
        
    }
    ngAfterViewInit(): void {
        this.terminal = new CryptoTraderConsole(this.host, this.consoleCommandService);
        window.addEventListener('resize', this.onResize.bind(this), { passive: true });
    }

    private onResize(): void {
        return this.terminal.fit();
    }
    ngOnDestroy(): void {
        window.removeEventListener('resize', this.onResize.bind(this));
        this.terminal.dispose();
    }
}
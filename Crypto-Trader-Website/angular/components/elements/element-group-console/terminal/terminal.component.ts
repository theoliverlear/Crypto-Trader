// terminal.component.ts
import {
    AfterViewInit,
    Component,
    ElementRef,
    OnDestroy,
    ViewChild
} from "@angular/core";
import { Terminal } from '@xterm/xterm';
import { FitAddon } from '@xterm/addon-fit';
import { WebLinksAddon } from '@xterm/addon-web-links';

@Component({
    selector: 'terminal',
    templateUrl: './terminal.component.html',
    styleUrls: ['./terminal.component.scss'],
    standalone: false
})
export class TerminalComponent implements AfterViewInit, OnDestroy {
    @ViewChild('host', { static: true }) host!: ElementRef<HTMLDivElement>;
    private terminal = new Terminal({
        convertEol: true,
        cursorBlink: true,
        fontSize: 14,
        theme: { 
            background: '#000000'
        }
    })
    private fit = new FitAddon();
    private links = new WebLinksAddon();
    private buffer = '';
    private prompt = '$ ';
    constructor() {
        
    }
    ngAfterViewInit(): void {
        this.terminal.loadAddon(this.fit);
        this.terminal.loadAddon(this.links);
        this.terminal.open(this.host.nativeElement);
        this.fit.fit();

        window.addEventListener('resize', this.onResize, { passive: true });
        this.terminal.onData(this.onData);

        this.terminal.writeln('Welcome to the in-browser console');
        this.terminal.writeln('Type "help" to see commands.');
        this.terminal.writeln('');
        this.showPrompt();
    }

    private onResize = () => this.fit.fit();

    private onData = (data: string) => {
        const code = data.charCodeAt(0);

        if (data === '\r') { // Enter
            const line = this.buffer.trim();
            this.terminal.write('\r\n');
            this.exec(line);
            this.buffer = '';
            this.showPrompt();
            return;
        }
        if (code === 127 || data === '\x7f') { // Backspace
            if (this.buffer.length) {
                this.buffer = this.buffer.slice(0, -1);
                this.terminal.write('\b \b');
            }
            return;
        }
        if (code >= 32 && code <= 126) { // Printable
            this.buffer += data;
            this.terminal.write(data);
        }
    };

    private showPrompt() {
        this.terminal.write(this.prompt);
    }

    private exec(input: string) {
        const [cmd, ...args] = input.split(/\s+/);
        switch (cmd) {
            case '':
                break;
            case 'help':
                this.terminal.writeln('Available: help, echo, time, clear');
                break;
            case 'echo':
                this.terminal.writeln(args.join(' '));
                break;
            case 'time':
                this.terminal.writeln(new Date().toString());
                break;
            case 'clear':
                this.terminal.clear();
                break;
            default:
                this.terminal.writeln(`Unknown command: ${cmd}`);
        }
    }

    ngOnDestroy(): void {
        window.removeEventListener('resize', this.onResize);
        this.terminal.dispose();
    }
}
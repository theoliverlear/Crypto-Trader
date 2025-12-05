import {Terminal} from "@xterm/xterm";
import {FitAddon} from "@xterm/addon-fit";
import {WebLinksAddon} from "@xterm/addon-web-links";
import {ElementRef, inject} from "@angular/core";
import {defaultTerminal} from "../../assets/consoleAssets";
import {
    ConsoleCommandService
} from "../../services/net/http/console/console-command.service";
import {catchError, concatMap, of, Subject, tap} from "rxjs";

export class CryptoTraderConsole {
    private _terminal: Terminal;
    private _fit: FitAddon;
    private _links: WebLinksAddon;
    private _buffer: string = '';
    private _elementRef: ElementRef<HTMLDivElement>;
    private readonly _consoleCommandService: ConsoleCommandService;
    private _command$: Subject<string>;
    private static readonly PROMPT_CHAR: string = '$ ';
    
    private static readonly RED = '\x1b[31m';
    private static readonly GREEN = '\x1b[32m';
    private static readonly YELLOW = '\x1b[33m';
    private static readonly CYAN = '\x1b[36m';
    private static readonly RESET = '\x1b[0m';
    private static readonly BOLD = '\x1b[1m';

    constructor(elementRef: ElementRef<HTMLDivElement>, 
                consoleCommandService: ConsoleCommandService) {
        this._elementRef = elementRef;
        this._terminal = defaultTerminal;
        this._fit = new FitAddon();
        this._links = new WebLinksAddon();
        this._consoleCommandService = consoleCommandService;
        this._command$ = new Subject<string>();
        this.initTerminal();
        this.initReactivePipeline();
    }

    private initTerminal() {
        this._terminal.loadAddon(this._fit);
        this._terminal.loadAddon(this._links);
        this._terminal.open(this._elementRef.nativeElement);
        this._fit.fit();
        this._terminal.onData(this.onData.bind(this));

        let welcomeText = CryptoTraderConsole.getBoldText('Welcome to the Crypto Trader console!');
        welcomeText = CryptoTraderConsole.getColoredText(welcomeText, CryptoTraderConsole.CYAN);
        this._terminal.writeln(welcomeText);
        let helpText = 'Type "help" to see commands.';
        this._terminal.writeln(helpText);
        this._terminal.writeln('');
        this.showPrompt();
    }

    private initReactivePipeline(): void {
        this._command$
            .pipe(
                concatMap((line) => {
                    if (!line) {
                        this.exec(line);
                        return of({consoleOutput: ""});
                    }
                    this.exec(line);
                    return this._consoleCommandService.executeCommand({command: line}).pipe(
                        catchError(err => {
                            this._terminal.writeln(
                                `${CryptoTraderConsole.RED}Error executing command: ${err?.message ?? err}${CryptoTraderConsole.RESET}`
                            );
                            return of({consoleOutput: ""});
                        })
                    );
                }),
                tap(response => {
                    if (response.consoleOutput) {
                        this._terminal.writeln(response.consoleOutput);
                    }
                    this.showPrompt();
                })
            )
            .subscribe();
    }

    private onData(data: string) {
        const code = data.charCodeAt(0);

        if (data === '\r') { // Enter
            const line = this._buffer.trim();
            this._terminal.write('\r\n');
            this._command$.next(line);

            this._buffer = '';
            return;
        }
        if (code === 127 || data === '\x7f') { // Backspace
            if (this._buffer.length) {
                this._buffer = this._buffer.slice(0, -1);
                this._terminal.write('\b \b');
            }
            return;
        }
        if (code >= 32 && code <= 126) { // Printable
            this._buffer += data;
            this._terminal.write(data);
        }
    }

    private showPrompt(): void {
        this._terminal.write(CryptoTraderConsole.PROMPT_CHAR);
    }

    private exec(input: string) {
        const [cmd, ...args] = input.split(/\s+/);
        switch (cmd) {
            case '':
                break;
            case 'time':
                this._terminal.writeln(new Date().toString());
                break;
            case 'clear':
                this._terminal.clear();
                break;
            default:
                // this._terminal.writeln(`${CryptoTraderConsole.RED}Unknown command: ${cmd}${CryptoTraderConsole.RESET}`);
        }
    }

    public fit(): void {
        this._fit.fit();
    }
    
    public isLocalCommand() {
        // TODO: Create enum and namespace for local commands.
    }
    
    public dispose(): void {
        this._terminal.dispose();
    }
    
    private static getColoredText(text: string, color: string): string {
        return `${color}${text}${CryptoTraderConsole.RESET}`;
    }
    
    private static getBoldText(text: string): string {
        return `${CryptoTraderConsole.BOLD}${text}${CryptoTraderConsole.RESET}`;
    }
}
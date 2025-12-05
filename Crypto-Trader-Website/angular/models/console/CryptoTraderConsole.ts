import {Terminal} from "@xterm/xterm";
import {FitAddon} from "@xterm/addon-fit";
import {WebLinksAddon} from "@xterm/addon-web-links";
import {ElementRef, inject} from "@angular/core";
import {defaultTerminal} from "../../assets/consoleAssets";
import {
    ConsoleCommandService
} from "../../services/net/http/console/console-command.service";
import {catchError, concatMap, of, Subject, tap} from "rxjs";
import {LocalConsoleCommand} from "./LocalConsoleCommand";

export class CryptoTraderConsole {
    private _terminal: Terminal;
    private _fit: FitAddon;
    private _links: WebLinksAddon;
    private _buffer: string = '';
    private _elementRef: ElementRef<HTMLDivElement>;
    private readonly _consoleCommandService: ConsoleCommandService;
    private _command$: Subject<string>;
    private static readonly PROMPT_CHAR: string = '$ ';
    private _history: string[];
    private _historyIndex: number;
    
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
        this._history = [];
        this._historyIndex = -1;
        this.initTerminal();
        this.initReactivePipeline();
    }

    private initTerminal(): void {
        this._terminal.loadAddon(this._fit);
        this._terminal.loadAddon(this._links);
        this._terminal.open(this._elementRef.nativeElement);
        this._fit.fit();
        this._terminal.onData(this.onData.bind(this));
        this._terminal.onKey(this.onKey.bind(this));
        this.writeDefaultText();
        this.showPrompt();
    }

    private writeDefaultText(): void {
        let welcomeText: string = CryptoTraderConsole.getBoldText('Welcome to the Crypto Trader console!');
        welcomeText = CryptoTraderConsole.getColoredText(welcomeText, CryptoTraderConsole.CYAN);
        this._terminal.writeln(welcomeText);
        let helpText = 'Type "help" to see commands.';
        this._terminal.writeln(helpText);
        this._terminal.writeln('');
    }

    private initReactivePipeline(): void {
        this._command$
            .pipe(
                concatMap((line) => {
                    if (!line) {
                        if (this.isLocalCommand(line)) {
                            this.exec(line);
                            return of({consoleOutput: ""});
                        }
                    }
                    if (this.isLocalCommand(line)) {
                        this.exec(line);
                        return of({consoleOutput: ""});
                    }
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
        const code: number = data.charCodeAt(0);

        if (data === '\r') { // Enter
            const line = this._buffer.trim();
            this._terminal.write('\r\n');

            if (line.length > 0) {
                if (this._history.length === 0 || this._history[this._history.length - 1] !== line) {
                    this._history.push(line);
                }
                this._historyIndex = this._history.length;
            }

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

    private onKey(event: { key: string; domEvent: KeyboardEvent }) {
        const { key, domEvent } = event;

        if (domEvent.key === 'ArrowUp') {
            domEvent.preventDefault();
            this.showPreviousHistory();
            return;
        }
        
        if (domEvent.key === 'ArrowDown') {
            domEvent.preventDefault();
            this.showNextHistory();
            return;
        }

        if (domEvent.key === 'Backspace' && domEvent.ctrlKey) {
            domEvent.preventDefault();
            this.deletePreviousWord();
            return;
        }
    }

    private deletePreviousWord(): void {
        if (!this._buffer.length) {
            return;
        }

        let i: number = this._buffer.length - 1;
        while (i >= 0 && this._buffer[i] === ' ') {
            i--;
        }
        
        while (i >= 0 && this._buffer[i] !== ' ') {
            i--;
        }

        this._buffer = this._buffer.slice(0, i + 1);
        this.clearCurrentLine();
        this.redrawPromptAndBuffer();
    }

    private clearCurrentLine(): void {
        this._terminal.write('\x1b[2K\r');
    }

    private redrawPromptAndBuffer(): void {
        this._terminal.write(CryptoTraderConsole.PROMPT_CHAR + this._buffer);
    }

    private showPreviousHistory(): void {
        if (this._history.length === 0) {
            return;
        }
        if (this._historyIndex > 0) {
            this._historyIndex--;
        } else {
            this._historyIndex = 0;
        }
        this._buffer = this._history[this._historyIndex] ?? '';
        this.clearCurrentLine();
        this.redrawPromptAndBuffer();
    }

    private showNextHistory(): void {
        if (this._history.length === 0) {
            return;
        }
        if (this._historyIndex < this._history.length - 1) {
            this._historyIndex++;
            this._buffer = this._history[this._historyIndex] ?? '';
        } else {
            this._historyIndex = this._history.length;
            this._buffer = '';
        }
        this.clearCurrentLine();
        this.redrawPromptAndBuffer();
    }
    
    removeTrailingSpaces(): void {
        this._buffer = this._buffer.trimEnd();
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
                this.writeDefaultText();
                break;
            default:
                this._terminal.writeln(`${CryptoTraderConsole.RED}Unknown command: ${cmd}${CryptoTraderConsole.RESET}`);
        }
    }

    public fit(): void {
        this._fit.fit();
    }
    
    public isLocalCommand(command: string): boolean {
        return LocalConsoleCommand.isLocalCommand(command);
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
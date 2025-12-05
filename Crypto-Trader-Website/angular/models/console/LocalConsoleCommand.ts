export enum LocalConsoleCommand {
    AUTH = "auth",
    CLEAR = "clear",
    TIME = "time"
}
export namespace LocalConsoleCommand {
    export function values(): LocalConsoleCommand[] {
        return [
            LocalConsoleCommand.AUTH,
            LocalConsoleCommand.CLEAR,
            LocalConsoleCommand.TIME
        ];
    }
    export function isLocalCommand(command: string): boolean {
        return values().includes(command as LocalConsoleCommand);
    }
}
export enum LocalConsoleCommand {
    CLEAR = "clear",
    TIME = "time"
}
export namespace LocalConsoleCommand {
    export function values(): LocalConsoleCommand[] {
        return [
            LocalConsoleCommand.CLEAR,
            LocalConsoleCommand.TIME
        ];
    }
    export function isLocalCommand(command: string): boolean {
        return values().includes(command as LocalConsoleCommand);
    }
}
export type ConsoleCommandRequest = {
    command: string
};

export type ConsoleCommandResponse = {
    consoleOutput: string,
    payload?: any
};
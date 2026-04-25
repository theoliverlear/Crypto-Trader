export type ConsoleCommandRequest = {
    commandText: string;
};

export type ConsoleCommandResponse = {
    consoleOutput: string;
    payload?: any;
};

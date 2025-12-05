import {Terminal} from "@xterm/xterm";

export const defaultTerminal: Terminal = new Terminal({
    convertEol: true,
    cursorBlink: true,
    fontSize: 14,
    theme: {
        background: '#171717'
    }
});
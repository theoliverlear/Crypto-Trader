export enum SimpleSortState {
    NONE = "remove",
    ASCENDING = "arrow_upward",
    DESCENDING = "arrow_downward"
}
export namespace SimpleSortState {
    export function values(): SimpleSortState[] {
        return [
            SimpleSortState.NONE,
            SimpleSortState.ASCENDING,
            SimpleSortState.DESCENDING
        ];
    }
    
    export function getNextState(state: SimpleSortState): SimpleSortState {
        const numStates: number = values().length;
        const nextSlot: number = (values().indexOf(state) + 1) % numStates;
        return SimpleSortState.values()[nextSlot];
    }
}
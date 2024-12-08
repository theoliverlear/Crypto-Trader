import {SwipeDirection} from "./models/SwipeDirection";

export interface SwipeEvent {
    direction: SwipeDirection;
    startX: number;
    startY: number;
    endX: number;
    endY: number;
}
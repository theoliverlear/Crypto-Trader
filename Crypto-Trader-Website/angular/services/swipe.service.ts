import {Injectable, NgZone} from "@angular/core";
import {SwipeEvent} from "../components/events/SwipeEvent";
import {SwipeDirection} from "../components/events/models/SwipeDirection";

@Injectable({
    providedIn: 'root'
})
export class SwipeService {
    private minSwipeDistance = 30;
    constructor(private ngZone: NgZone) {
        console.log('SwipeService loaded');
    }
    detectSwipe(element: HTMLElement, callback: (event: SwipeEvent) => void): void {
        console.log('Detecting swipe');
        let touchStartX = 0;
        let touchStartY = 0;
        let touchEndX = 0;
        let touchEndY = 0;

        element.addEventListener('touchstart', (event) => {
            touchStartX = event.changedTouches[0].screenX;
            touchStartY = event.changedTouches[0].screenY;
        });

        element.addEventListener('touchend', (event) => {
            touchEndX = event.changedTouches[0].screenX;
            touchEndY = event.changedTouches[0].screenY;
            this.ngZone.run(() => {
                this.handleSwipe(touchStartX, touchStartY, touchEndX, touchEndY, callback);
            });
        });
    }

    private handleSwipe(startX: number, startY: number, endX: number, endY: number, callback: (event: SwipeEvent) => void): void {
        const deltaX = endX - startX;
        const deltaY = endY - startY;

        if (Math.abs(deltaX) > this.minSwipeDistance || Math.abs(deltaY) > this.minSwipeDistance) {
            const direction = this.getSwipeDirection(deltaX, deltaY);
            callback({
                direction,
                startX,
                startY,
                endX,
                endY
            });
        }
    }

    private getSwipeDirection(deltaX: number, deltaY: number): SwipeDirection {
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            return deltaX > 0 ? SwipeDirection.RIGHT : SwipeDirection.LEFT;
        } else {
            return deltaY > 0 ? SwipeDirection.DOWN : SwipeDirection.UP;
        }
    }
}
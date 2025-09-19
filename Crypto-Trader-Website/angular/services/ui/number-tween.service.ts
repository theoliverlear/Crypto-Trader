// number-tween.service.ts
import {Injectable} from '@angular/core';
import {Observable, animationFrames} from 'rxjs';
import {map, takeWhile} from 'rxjs/operators';

export type EasingFn = (tick: number) => number;

@Injectable({
    providedIn: 'root'
})
export class NumberTweenService {
    private static defaultEase: EasingFn = (tick: number) => 1 - Math.pow(1 - tick,  3);

    animate(from: number,
            to: number,
            durationMs: number = 500,
            easing: EasingFn = NumberTweenService.defaultEase): Observable<number> {
        const start: number = performance.now();
        return animationFrames().pipe(
            map(() => (performance.now() - start) / durationMs),
            takeWhile(t => t < 1, true),
            map(ticked => {
                const clamped: number = Math.min(1, Math.max(0, ticked));
                const ease: number = easing(clamped);
                return from + (to - from) * ease;
            })
        );
    }
}

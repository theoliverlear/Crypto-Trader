// number-tween.service.ts
import { Injectable } from '@angular/core';
import { animationFrames, Observable } from 'rxjs';
import { map, takeWhile } from 'rxjs/operators';

export type EasingFn = (tick: number) => number;

@Injectable({
    providedIn: 'root',
})
export class NumberTweenService {
    private static readonly defaultEase: EasingFn = (tick: number): number => 1 - Math.pow(1 - tick, 3);

    animate(
        fromNum: number,
        toNum: number,
        durationMs: number = 500,
        easing: EasingFn = NumberTweenService.defaultEase,
    ): Observable<number> {
        const start: number = performance.now();
        return animationFrames().pipe(
            map((): number => (performance.now() - start) / durationMs),
            takeWhile((progress: number): boolean => progress < 1, true),
            map((ticked: number): number => {
                const clamped: number = Math.min(1, Math.max(0, ticked));
                const ease: number = easing(clamped);
                return fromNum + (toNum - fromNum) * ease;
            }),
        );
    }
}

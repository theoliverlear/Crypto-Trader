import {
    animate,
    query,
    stagger,
    style,
    transition,
    trigger,
    type AnimationTriggerMetadata,
} from '@angular/animations';

export const listStagger: AnimationTriggerMetadata = trigger('listStagger', [
    transition('* => *', [
        query(
            ':enter',
            [
                style({ opacity: 0, transform: 'translateY(8px)' }),
                stagger(800, [
                    animate(
                        '600ms ease-out',
                        style({ opacity: 1, transform: 'translateY(0)' }),
                    ),
                ]),
            ],
            { optional: true },
        ),
    ]),
]);

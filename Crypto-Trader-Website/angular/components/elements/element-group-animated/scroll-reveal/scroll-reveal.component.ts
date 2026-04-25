// scroll-reveal.component.ts
import {
    AfterViewInit,
    Component,
    ElementRef,
    HostBinding,
    Input,
    OnDestroy,
} from '@angular/core';

@Component({
    selector: 'scroll-reveal',
    standalone: false,
    templateUrl: './scroll-reveal.component.html',
    styleUrls: ['./scroll-reveal.component.scss'],
})
export class ScrollRevealComponent implements AfterViewInit, OnDestroy {
    @HostBinding('class.in-view') private inView: boolean = false;

    @Input() public threshold: number = 0.2;

    private observer?: IntersectionObserver;

    constructor(private readonly element: ElementRef<HTMLElement>) {}

    public ngAfterViewInit(): void {
        if (typeof window !== 'undefined' && 'IntersectionObserver' in window) {
            this.observer = new IntersectionObserver(
                (entries: IntersectionObserverEntry[]): void => {
                    for (const entry of entries) {
                        if (entry.isIntersecting) {
                            this.inView = true;
                            if (this.observer) {
                                this.observer.unobserve(entry.target);
                            }
                        }
                    }
                },
                { threshold: this.threshold },
            );
            this.observer.observe(this.element.nativeElement);
        } else {
            this.inView = true;
        }
    }

    public ngOnDestroy(): void {
        if (this.observer) {
            this.observer.disconnect();
        }
    }
}

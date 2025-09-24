// transparency-promo.component.ts
import { AfterViewInit, Component, ElementRef, HostBinding, OnDestroy } from "@angular/core";
import {transparencyParagraph} from "../../../../assets/textAssets";
import {
    docsElementLink,
    repositoryElementLink
} from "../../../../assets/elementLinkAssets";
import {TagType} from "@theoliverlear/angular-suite";

@Component({
    selector: 'transparency-promo',
    standalone: false,
    templateUrl: './transparency-promo.component.html',
    styleUrls: ['./transparency-promo.component.scss']
})
export class TransparencyPromoComponent implements AfterViewInit, OnDestroy {
    @HostBinding('class.in-view') inView = false;

    private observer?: IntersectionObserver;

    constructor(private readonly elRef: ElementRef<HTMLElement>) {}

    ngAfterViewInit(): void {
        if (typeof window !== 'undefined' && 'IntersectionObserver' in window) {
            this.observer = new IntersectionObserver((entries) => {
                for (const entry of entries) {
                    if (entry.isIntersecting) {
                        this.inView = true;
                        if (this.observer) {
                            this.observer.unobserve(entry.target);
                        }
                    }
                }
            }, { threshold: 0.2 });
            this.observer.observe(this.elRef.nativeElement);
        } else {
            this.inView = true;
        }
    }

    ngOnDestroy(): void {
        if (this.observer) {
            this.observer.disconnect();
        }
    }

    protected readonly TagType = TagType;
    protected readonly transparencyParagraph = transparencyParagraph;
    protected readonly repositoryElementLink = repositoryElementLink;
    protected readonly docsElementLink = docsElementLink;
}

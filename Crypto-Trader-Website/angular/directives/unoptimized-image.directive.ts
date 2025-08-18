import {AfterViewInit, Directive, ElementRef, OnDestroy} from "@angular/core";

@Directive({
    selector: '[unoptimizedImage]'
})
export class UnoptimizedImageDirective implements AfterViewInit, OnDestroy {
    private mutationObserver: MutationObserver;
    constructor(private imageElement: ElementRef<HTMLImageElement>) {
        console.log('UnoptimizedImageDirective loaded');
    }
    ngAfterViewInit() {
        this.removeInlineStyles();
        this.removeInlineAttributes();
        this.mutationObserver = new MutationObserver(() => {
            this.removeInlineStyles();
            this.removeInlineAttributes();
        });
        this.mutationObserver.observe(this.imageElement.nativeElement, {
            attributes: true,
            attributeFilter: ['style', 'sizes']
        });
    }

    private removeInlineStyles() {
        const stylesToRemove = ['position', 'width', 'height', 'inset'];
        stylesToRemove.forEach(style => {
            this.imageElement.nativeElement.style.removeProperty(style);
        });
    }
    private removeInlineAttributes() {
        const attributesToRemove = ['sizes'];
        attributesToRemove.forEach(attribute => {
            this.imageElement.nativeElement.removeAttribute(attribute);
        });
    }
    ngOnDestroy() {
        if (this.mutationObserver) {
            this.mutationObserver.disconnect();
        }
    }
}
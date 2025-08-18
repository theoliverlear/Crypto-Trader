import { Directive, ElementRef, Renderer2, HostListener } from '@angular/core';

@Directive({
    selector: '[undraggable]'
})
export class UndraggableDirective {
    constructor(private element: ElementRef, private renderer: Renderer2) {
        this.renderer.setAttribute(this.element.nativeElement, 'draggable', 'false');
    }
    @HostListener('dragstart', ['$event'])
    onDragStart(event: DragEvent) {
        event.preventDefault();
    }
}
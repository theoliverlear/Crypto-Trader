import {
    Component,
    Inject,
    Input,
    OnChanges,
    Renderer2,
    SimpleChanges
} from "@angular/core";
import {DOCUMENT} from "@angular/common";

@Component({
    selector: 'ss-head',
    templateUrl: './ss-head.component.html'
})
export class SsHeadComponent implements OnChanges{
    static readonly DEFAULT_FAVICON = 'assets/images/favicon/favicon.ico';
    @Input() title: string;
    @Input() favicon: string = SsHeadComponent.DEFAULT_FAVICON;
    constructor(private renderer: Renderer2, @Inject(DOCUMENT) private document: Document) {
        this.addFavicon(this.favicon);
    }
    ngOnChanges(changes: SimpleChanges): void {
        if (changes['title']) {
            this.updateTitle();
        }
    }
    private updateTitle() {
        if (this.title) {
            this.document.title = this.title;
        }
    }

    private addFavicon(path: string) {
        const link = this.renderer.createElement('link');
        link.rel = 'icon';
        link.href = path;
        this.renderer.appendChild(this.document.head, link);
    }
    private clearExisting(selector: string) {
        const existing: NodeListOf<Element> = this.document.querySelectorAll(selector);
        existing.forEach((element) => {
            this.renderer.removeChild(this.document.head, element);
        });
    }
}
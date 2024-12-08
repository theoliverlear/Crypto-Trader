import {
    AfterViewInit,
    ChangeDetectorRef,
    Component,
    ElementRef,
    Input,
    ViewChild
} from "@angular/core";
import {ImageAsset} from "../../../assets/imageAssets";


@Component({
    selector: 'ss-img',
    templateUrl: './ss-img.component.html',
    styleUrls: ['./ss-img-style.component.css']
})
export class SsImgComponent implements AfterViewInit {
    @Input() imageAsset: ImageAsset;
    @Input() childId: string;
    @Input() childClass: string;
    @ViewChild('imageElement') imageElement: ElementRef;
    constructor(private changeDetector: ChangeDetectorRef) {
        console.log('SsImgComponent loaded');
    }
    ngAfterViewInit() {
        if (this.imageAsset.src === '') {

        }
        this.changeDetector.detectChanges();
    }
    addClassToImageElement(className: string) {
        this.imageElement.nativeElement.classList.add(className);
    }
}
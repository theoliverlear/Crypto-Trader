import {
    Component,
    EventEmitter,
    HostBinding, HostListener,
    Input,
    OnInit,
    Output
} from "@angular/core";
import {ElementSize} from "../../../models/ElementSize";
import {ButtonText} from "./models/ButtonText";
import {ButtonPosition} from "./models/ButtonPosition";

@Component({
    selector: 'ss-button',
    templateUrl: './ss-button.component.html',
    styleUrls: ['./ss-button.component.css']
})
export class SsButtonComponent implements OnInit{
    @Input() text: ButtonText;
    @Input() size: ElementSize;
    @Input() buttonPosition: ButtonPosition;
    @HostBinding('style.align-self') alignSelf: string;
    @Output() buttonClick: EventEmitter<void> = new EventEmitter<void>();
    constructor() {
        console.log('SsButtonComponent loaded');
    }
    ngOnInit() {
        this.setAlignSelf();
    }

    emitButtonClick(): void {
        this.buttonClick.emit();
    }
    @HostListener('click')
    onClick(): void {
        this.emitButtonClick();
    }
    private setAlignSelf(): void {
        switch (this.buttonPosition) {
            case ButtonPosition.START:
                this.alignSelf = ButtonPosition.START;
                break;
            case ButtonPosition.END:
                this.alignSelf = ButtonPosition.END;
                break;
            case ButtonPosition.CENTER:
                this.alignSelf = ButtonPosition.CENTER;
                break;
            default:
                this.alignSelf = ButtonPosition.INHERIT;
        }
    }
}
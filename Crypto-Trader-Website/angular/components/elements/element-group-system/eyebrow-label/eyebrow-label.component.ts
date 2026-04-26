// eyebrow-label.component.ts
import { Component, Input } from '@angular/core';

@Component({
    selector: 'eyebrow-label',
    standalone: false,
    templateUrl: './eyebrow-label.component.html',
    styleUrls: ['./eyebrow-label.component.scss'],
})
export class EyebrowLabelComponent {
    @Input() public text: string = '';
}

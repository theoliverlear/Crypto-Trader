// badge-grid.component.ts
import { Component, Input } from '@angular/core';

@Component({
    selector: 'badge-grid',
    standalone: false,
    templateUrl: './badge-grid.component.html',
    styleUrls: ['./badge-grid.component.scss'],
})
export class BadgeGridComponent {
    @Input() public items: string[] = [];
    @Input() public columns: number = 2;
    @Input() public ariaLabel: string = 'Items';
    @Input() public staggerAnimation: boolean = true;
}

// simple-sort-icon.component.ts
import {
    ChangeDetectorRef,
    Component,
    EventEmitter,
    HostListener, Input,
    OnChanges,
    Output, SimpleChanges
} from "@angular/core";
import {SimpleSortState} from "../../../models/SimpleSortState";

@Component({
    selector: 'simple-sort-icon',
    templateUrl: './simple-sort-icon.component.html',
    styleUrls: ['./simple-sort-icon.component.scss'],
    standalone: false
})
export class SimpleSortIconComponent implements OnChanges {
    @Input() currentSortState: SimpleSortState = SimpleSortState.NONE;
    @Output() sortStateChanged: EventEmitter<SimpleSortState> = new EventEmitter<SimpleSortState>();
    constructor(private changeDetector: ChangeDetectorRef) {
    }
    
    ngOnChanges(simpleChanges: SimpleChanges): void {
        if (simpleChanges.currentSortState) {
            this.currentSortState = simpleChanges.currentSortState.currentValue;
        }
    }
    
    getStateText(): string {
        return this.currentSortState;
    }
    
    @HostListener('click')
    onClick(): void {
        this.nextSortState();
        this.changeDetector.detectChanges();
        this.emitSortState();
    }
    
    public nextSortState(): void {
        this.currentSortState = SimpleSortState.getNextState(this.currentSortState);
    }
    
    emitSortState(): void {
        this.sortStateChanged.emit(this.currentSortState);
    }
}
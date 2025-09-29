// search-input.component.ts
import {
    Component,
    EventEmitter,
    HostBinding,
    Input,
    Output
} from "@angular/core";
import {FormControl} from "@angular/forms";
import {map, Observable, startWith} from "rxjs";

@Component({
    selector: 'search-input',
    templateUrl: './search-input.component.html',
    styleUrls: ['./search-input.component.scss'],
    standalone: false
})
export class SearchInputComponent {
    searchControl: FormControl<string> = new FormControl('');
    @Input() options: string[] = [];
    filteredOptions: Observable<string[]> = this.searchControl.valueChanges.pipe(
        startWith(''),
        map(value => this._filter(value || ''))
    );
    @Input() isEnabled: boolean = true;
    
    @HostBinding('class.disabled') get disabled(): boolean {
        return !this.isEnabled;
    }
    
    @Output() selectionChange: EventEmitter<string> = new EventEmitter();
    constructor() {
        
    }

    emitSelectionChange() {
        this.selectionChange.emit(this.searchControl.value);
    }
    
    clear(): void {
        this.searchControl.setValue('');
    }

    private _filter(value: string): string[] {
        const filterValue: string = value.toLowerCase();
        return this.options.filter(option =>
            option.toLowerCase().includes(filterValue)
        );
    }
}
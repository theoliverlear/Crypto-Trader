// search-input.component.ts
import {Component, Input} from "@angular/core";
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
    constructor() {
        
    }

    clear(): void {
        this.searchControl.setValue('');
    }

    private _filter(value: string): string[] {
        const filterValue = value.toLowerCase();
        return this.options.filter(option =>
            option.toLowerCase().includes(filterValue)
        );
    }
}
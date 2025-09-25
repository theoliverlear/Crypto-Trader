// search-input.component.ts
import { Component } from "@angular/core";
import {FormControl} from "@angular/forms";
import {map, Observable, startWith} from "rxjs";

@Component({
    selector: 'search-input',
    templateUrl: './search-input.component.html',
    styleUrls: ['./search-input.component.scss'],
    standalone: false
})
export class SearchInputComponent {
    searchControl = new FormControl('');
    // TODO: Pull from an input. This is a placeholder.
    allOptions: string[] = ['Bitcoin', 'Ethereum', 'Cardano', 'Polkadot'];
    filteredOptions: Observable<string[]> = this.searchControl.valueChanges.pipe(
        startWith(''),
        map(value => this._filter(value || ''))
    );
    constructor() {
        
    }

    private _filter(value: string): string[] {
        const filterValue = value.toLowerCase();
        return this.allOptions.filter(option =>
            option.toLowerCase().includes(filterValue)
        );
    }
}
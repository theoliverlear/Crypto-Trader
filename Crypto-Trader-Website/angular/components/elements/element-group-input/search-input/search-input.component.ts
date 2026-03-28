// search-input.component.ts
import { Component, EventEmitter, HostBinding, Input, Output } from '@angular/core';
import { FormControl } from '@angular/forms';
import { map, Observable, startWith } from 'rxjs';

/** A search input that filters options.
 *
 */
@Component({
    selector: 'search-input',
    templateUrl: './search-input.component.html',
    styleUrls: ['./search-input.component.scss'],
    standalone: false,
})
export class SearchInputComponent {
    protected searchControl: FormControl<string | null> = new FormControl<string | null>('');
    @Input() public options: string[] = [];
    protected filteredOptions: Observable<string[]> = this.searchControl.valueChanges.pipe(
        startWith(''),
        map((value: string | null): string[] => this._filter(value || '')),
    );
    @Input() public isEnabled: boolean = true;

    /** Whether the input is disabled.
     * @returns If the input is disabled.
     */
    @HostBinding('class.disabled') protected get disabled(): boolean {
        return !this.isEnabled;
    }

    @Output() public selectionChange: EventEmitter<string> = new EventEmitter<string>();
    constructor() {}

    /** Emits the selection change event.
     *
     */
    protected emitSelectionChange(): void {
        if (!this.searchControl.value) {
            return;
        }
        this.selectionChange.emit(this.searchControl.value);
    }

    /** Clears the search input.
     *
     */
    protected clear(): void {
        this.searchControl.setValue('');
    }

    private _filter(value: string | null): string[] {
        if (!value) {
            return [];
        }
        const filterValue: string = value.toLowerCase();
        return this.options.filter((option: string): boolean =>
            option.toLowerCase().includes(filterValue),
        );
    }
}

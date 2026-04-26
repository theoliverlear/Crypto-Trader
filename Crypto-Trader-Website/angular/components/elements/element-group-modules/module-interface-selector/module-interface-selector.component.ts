// module-interface-selector.component.ts
import { Component, Input } from '@angular/core';
import { ModuleInfo } from '@models/module/ModuleInfo';

@Component({
    selector: 'module-interface-selector',
    standalone: false,
    templateUrl: './module-interface-selector.component.html',
    styleUrls: ['./module-interface-selector.component.scss'],
})
export class ModuleInterfaceSelectorComponent {
    @Input() public modules: ModuleInfo[] = [];

    protected activeInterface: string = 'website';

    selectInterface(name: string): void {
        this.activeInterface = name.toLowerCase();
    }

    getActiveInterface(): ModuleInfo | undefined {
        return this.modules.find(
            (m: ModuleInfo): boolean =>
                m.name.toLowerCase() === this.activeInterface,
        );
    }
}
